package welcome

import Presence
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalDesktopApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composeWindow
import data.KotaroFile
import data.KotaroProject
import data.SceneData
import dev.pengie.kotaro.assets.Path
import editor.EditorTheme
import editor.components.EditorTextField
import openEditor
import java.awt.Desktop
import java.awt.FileDialog
import java.io.File

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeWindow() {
    EditorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val state = remember { mutableStateOf(WelcomeState.WELCOME) }
            AnimatedVisibility(
                visible = state.component1() == WelcomeState.WELCOME,
                enter = slideInHorizontally(initialOffsetX = { -it }),
                exit = slideOutHorizontally(targetOffsetX = { -it })
            ) {
                WelcomeScreen(state)
            }
            AnimatedVisibility(
                visible = state.component1() == WelcomeState.NEW_PROJECT,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                NewProjectScreen(state)
            }
        }
    }
}

@OptIn(ExperimentalDesktopApi::class)
@Composable
fun NewProjectScreen(state: MutableState<WelcomeState>) {
    var projectName by remember { mutableStateOf("Untitled") }
    var projectDirectory by remember { mutableStateOf("${System.getProperty("user.home")}\\Documents\\Kotaro\\${projectName}") }
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier.fillMaxSize().mouseClickable {
        focusManager.clearFocus(true)
    }) {
        Row(modifier = Modifier.padding(6.dp)) {
            var hoverBack by remember { mutableStateOf(false) }
            Icon(
                Icons.Sharp.ArrowBack,
                contentDescription = "Back",
                tint = if(hoverBack) Color.Gray else Color.White,
                modifier = Modifier.
                    clickable {
                        state.component2().invoke(WelcomeState.WELCOME)
                    }.
                    align(Alignment.CenterVertically).
                    pointerMoveFilter(
                        onEnter = {
                            hoverBack = true
                            true
                        },
                        onExit = {
                            hoverBack = false
                            true
                        }
                    )
            )
            Text(text = "New project", modifier = Modifier.align(Alignment.CenterVertically).padding(start = 30.dp))
        }
        Divider(color = Color.Gray, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp))
        Column(modifier = Modifier.padding(6.dp)) {
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Text(text = "Project Name:", fontSize = 15.sp, modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterVertically))
                EditorTextField(projectName, onValueChange = {
                    projectName = it
                    val dir = projectDirectory.split("\\")
                    if(dir.size < 2)
                        return@EditorTextField
                    projectDirectory = dir.filterIndexed { index, s -> index != dir.size - 1 }.reduce { l, r -> "$l\\$r"} + "\\$projectName"
                }, modifier = Modifier.align(Alignment.CenterVertically).width(450.dp))
            }
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Text(text = "Project Directory:", fontSize = 15.sp, modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterVertically))
                EditorTextField(projectDirectory, onValueChange = {
                    projectDirectory = it
                }, modifier = Modifier.align(Alignment.CenterVertically).width(427.dp))
            }
            var hover by remember { mutableStateOf(false) }
            Surface(modifier = Modifier
                .pointerMoveFilter(
                    onEnter = {
                        hover = true
                        true
                    }, onExit = {
                        hover = false
                        true
                    }).
                clickable {
                    // Create Default Project
                    val defaultProject = KotaroProject(projectName, projectName, "/assets", arrayOf(
                        SceneData.createDefaultScene()
                    ))
                    File(projectDirectory).mkdirs()
                    File("$projectDirectory\\assets").mkdir()
                    KotaroFile.write("$projectDirectory\\project.kotaro", defaultProject)
                    Presence.createPresence(defaultProject)
                    openEditor!!.component2().invoke(true)
            }, elevation = if(hover) 4.dp else 2.dp, shape = RoundedCornerShape(4.dp)) {
                Text(text = "Create Project", fontSize = 15.sp, modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(state: MutableState<WelcomeState>) = Row {
    Surface(modifier = Modifier.fillMaxWidth(0.4f).fillMaxHeight(), elevation = 1.dp) {
        Column {
            Surface(modifier = Modifier.fillMaxWidth(), elevation = 2.dp) {
                Text(
                    text = "Recent Projects",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            LazyColumn {

            }
        }

    }
    Divider(modifier = Modifier.width(1.dp).fillMaxHeight(), color = Color.White.copy(alpha = 0.4f))
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.5f).fillMaxHeight(0.7f)) {
                var hoverTop by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)
                    .background(if (hoverTop) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.0f))
                    .pointerMoveFilter(
                        onEnter = {
                            hoverTop = true
                            true
                        }, onExit = {
                            hoverTop = false
                            true
                        }).clickable {
                        state.component2().invoke(WelcomeState.NEW_PROJECT)
                }) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Add",
                            modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(0.7f)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "New Project",
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
                var hoverBottom by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .background(if (hoverBottom) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.0f))
                    .pointerMoveFilter(
                        onEnter = {
                            hoverBottom = true
                            true
                        }, onExit = {
                            hoverBottom = false
                            true
                        }).clickable {
                        var dir = File("${System.getProperty("user.home")}\\Documents\\Kotaro")
                        if(!dir.exists())
                            dir = File("${System.getProperty("user.home")}\\Documents")
                        val dialog = FileDialog(composeWindow, "Open Project File", FileDialog.LOAD).apply {
                            this.directory = dir.absolutePath
                            this.setFilenameFilter { _, name -> name.endsWith(".kotaro") }
                            this.isVisible = true
                        }
                        if(dialog.file != null) {
                            val path = Path("${dialog.directory}${dialog.file}")
                            if(path.name == "project") {
                                val project = KotaroFile.read(path)
                                if(project != null) {
                                    Presence.createPresence(project)
                                    openEditor!!.component2().invoke(true)
                                }
                            }
                        }
                }) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Icon(
                            Icons.Rounded.ArrowForward,
                            contentDescription = "Open",
                            modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(0.7f)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Open Project",
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}