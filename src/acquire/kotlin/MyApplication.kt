package acquire.kotlin

import acquire.kotlin.recoup.RecoupInterface
import acquire.kotlin.recoup.action.LocalRequest
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

/**
 * @Author Nicholas Pinney
 */
class MyApplication : Application() {

    fun main(args: Array<String>) { launch(*args) }

    override fun start(primaryStage: Stage) {

        val mainScene = Scene(Pane(), 475.0, 425.0)
        ScreenController.stage = primaryStage
        ScreenController.scene = mainScene
        ScreenController.bindSceneWithStage()

        //ScreenController.addScene("firstLogin", FirstLogin())
        ScreenController.addScene("pinLogin", PinLogin())
        ScreenController.addScene("taskSelectionScene", TaskSelect())


        /*try { //TODO: 3 - To test acquire.kotlin.main interface quickly, comment this try catch & up to line 40
            val file = File(Config.readPathToCredFile())
            if (!file.exists()) {
                throw FileNotFoundException()
            }
            acquire.kotlin.ScreenController.activateScene("pinLogin")
        } catch (e: FileNotFoundException) {
            println("Cred.txt not found")
            acquire.kotlin.ScreenController.activateScene("firstLogin")
        }

        primaryStage.setOnCloseRequest {
            acquire.kotlin.SeleniumMethods.exitDriver()
        }*/

        ScreenController.stage!!.title = "Recoup Automation Software"
        ScreenController.stage!!.isResizable = true
        ScreenController.stage!!.show() //TODO: 5
        ScreenController.activateScene("taskSelectionScene") //TODO: 6 - Last
        //ScreenController.activateScene("mainInterface") //TODO: 6 - Last
    }
}