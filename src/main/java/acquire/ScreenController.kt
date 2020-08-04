package acquire

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
    val mapOfCustomPanes = mutableMapOf<String, Pane>()

    private var activeScreen: String? = null
    private var previousScreen = "taskSelectionScene"

    fun bindSceneWithStage() {
        stage!!.scene = scene!!
    }

    fun addScene(name: String, pane: Pane) {
        mapOfCustomPanes[name] = pane
        //println()
        //mapOfCustomPanes.forEach { println(it) }
    }

    fun removeScene(name: String?) {
        //println("Removing pane: $name")
        mapOfCustomPanes.remove(name)
        //println()
        //mapOfCustomPanes.forEach { println(it) }
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