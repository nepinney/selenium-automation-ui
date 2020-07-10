package acquire.kotlin

import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.util.*

/**
 * @Author Nicholas Pinney
 */
object ScreenController {

    var stage: Stage? = null
    var scene: Scene? = null
    private val mapOfCustomPanes = HashMap<String, Pane>()

    private var activeScreen: String? = null
    private var previousScreen = "taskSelectionScene"

    fun bindSceneWithStage() {
        stage!!.scene = scene!!
    }

    fun addScene(name: String, pane: Pane) {
        mapOfCustomPanes[name] = pane
    }

    fun removeScene(name: String?) {
        mapOfCustomPanes.remove(name)
    }

    fun activateScene(name: String?) {
        if (!activeScreen.isNullOrBlank()) {
            previousScreen = activeScreen!!
        }
        activeScreen = name
        /*println("Active Screen: $activeScreen")
        println("Previous Screen: $previousScreen")
        println()*/
        scene!!.root = mapOfCustomPanes[name]
    }

    fun activatePreviousScreen() {
        scene!!.root = mapOfCustomPanes[previousScreen]
        activeScreen = previousScreen
    }
}