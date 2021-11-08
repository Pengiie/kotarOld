import dev.pengie.kotaro.Application
import dev.pengie.kotaro.DesktopConfig
import dev.pengie.kotaro.scene.SceneLibrary
import scenes.GameScene

fun main() {
    SceneLibrary.registerScene(GameScene)
    Application(config = DesktopConfig().title("Sandbox"), startScene = 0)
}