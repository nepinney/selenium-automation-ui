package acquire.kotlin

import javafx.application.Preloader
import javafx.concurrent.Task
import javafx.event.EventHandler
import javafx.scene.Scene

import javafx.stage.Stage
import javafx.stage.StageStyle

/**
 * @Author Nicholas Pinney
 */
class SplashPreLoader : Preloader() {

    private var stage: Stage? = null
    private var splashScene: SplashScene? = null

    override fun start(stage: Stage) {
        //val intro =
        splashScene = SplashScene()
        this.stage = stage
        stage.scene = Scene(splashScene, 475.0, 415.0)
        stage.initStyle(StageStyle.UNDECORATED)
        stage.isAlwaysOnTop = true
        stage.show()

        val task: Task<SplashModel> = object : Task<SplashModel>() {
            override fun call(): SplashModel {
                val dataModel = SplashModel()
                dataModel.addLoadingStatus("Starting...")
                updateMessage(dataModel.currentStatus.get())
                dataModel.loadWebDriver()
                dataModel.addLoadingStatus("Navigating to BMC...")
                updateMessage(dataModel.currentStatus.get())
                dataModel.navigateToBMC()
                dataModel.addLoadingStatus("Completed, starting acquire.kotlin.main...")
                updateMessage(dataModel.currentStatus.get())
                return dataModel
            }
        }
        task.onSucceeded = EventHandler {
            handleStateChangeNotification("completed")
            ScreenController.stage?.isAlwaysOnTop = true
            ScreenController.stage?.show()
            val cacheAlert = SeleniumMethods.driver?.switchTo()?.alert()
            if (cacheAlert != null) {
                println("Got popup: " + cacheAlert.text)
                cacheAlert.accept()
            }
            ScreenController.stage?.isAlwaysOnTop = false
        }
        splashScene!!.statusMessageProperty().bind(task.messageProperty())
        Thread(task).start()
    }

    private fun handleStateChangeNotification(evt: String) {
        if (evt == "completed") {
            stage!!.hide()
        }
    }
}