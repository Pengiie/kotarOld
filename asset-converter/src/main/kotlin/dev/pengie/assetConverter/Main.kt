package dev.pengie.assetConverter

import dev.pengie.assetConverter.image.ImageParser
import dev.pengie.assetConverter.mesh.MeshParser
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

val argsMap: HashMap<String, String> = hashMapOf()

fun main(args: Array<String>) {
    if(args.isNotEmpty()) {
        val path = Path(args[0])

        args.forEachIndexed { i, arg ->
            if(i > 0 && args.contains("=")) {
                val a = arg.split("=")
                argsMap[a[0].lowercase()] = a[1]
            }
        }

        if(path.isDirectory()) {
            val file = File(path.fullPath)
            val stack: ArrayDeque<File> = ArrayDeque()
            stack.addAll(file.listFiles()!!)
            while(stack.isNotEmpty()) {
                val f = stack.last()
                stack.removeLast()
                if(f.isDirectory)
                    stack.addAll(f.listFiles()!!)
                else
                    processFile(Path(f.absolutePath))
            }
        } else processFile(path)
    }
}

private fun processFile(path: Path) {
    if(path.extension == "ktasset")
        return

    val file = File("${path.fullPath}.ktasset")
    if(file.exists()) {
        val attribs = Files.readAttributes(Paths.get(path.fullPath), BasicFileAttributes::class.java)
        val input = FileInputStream(file)
        val out = input.readNBytes(17)
        input.close()
        val mostRecent = Bytes.toLong(out[9], out[10], out[11], out[12], out[13], out[14], out[15], out[16])
        if(attribs.lastModifiedTime().toMillis() == mostRecent) {
            println("[INFO] File ${path.name}.${path.extension} already processed, skipping...")
            return
        }
    }
    val bytes = IO.readBytes(path)
    val assetBytes = when(path.extension) {
        "png" -> ImageParser.parsePNG(bytes, path)
        "obj" -> MeshParser.parseOBJ(bytes, path)
        else -> {
            println("[INFO] Don't know how to process file ${path.name}.${path.extension}, skipping...")
            return
        }
    }

    val writer = FileOutputStream(file)
    writer.write(assetBytes)
    writer.close()
    println("[INFO] Wrote to ${file.name}")
}