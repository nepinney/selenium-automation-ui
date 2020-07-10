package acquire.kotlin

import acquire.kotlin.recoup.manual.ManualInterface
import acquire.kotlin.recoup.manual.ManualMenuComponent
import acquire.kotlin.recoup.scan.ScanInterface
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TaskSelect : AnchorPane() {

    @FXML private lateinit var interfaceRoot: AnchorPane

    private fun configureRootContainer(): VBox {
        val rootContainer = VBox()

        val sceneTitle = Label("Select Task")
        sceneTitle.style = "-fx-font-size: 200%;"

        val recoupTitle = Label("Recuperation")
        recoupTitle.style = "-fx-font-weight: bold;"

        val recoupManualTasksButton = Button("Manual tasks")
        val recoupScanButton = Button("Bulk Operations")

        val macTitle = Label("MACs")
        macTitle.style = "-fx-font-weight: bold;"

        val macButton = Button("MAC Provisioning")
        val nodesAddedToVbox = listOf(sceneTitle, recoupTitle, recoupManualTasksButton, recoupScanButton, macTitle, macButton)

        AnchorPane.setBottomAnchor(rootContainer, 0.0)
        AnchorPane.setLeftAnchor(rootContainer, 0.0)
        AnchorPane.setRightAnchor(rootContainer, 0.0)
        AnchorPane.setTopAnchor(rootContainer, 0.0)
        rootContainer.spacing = 5.0
        rootContainer.alignment = Pos.CENTER

        nodesAddedToVbox.forEach { rootContainer.children.add(it) }

        recoupManualTasksButton.onAction = javafx.event.EventHandler {
            ScreenController.addScene("recoupInterface", ManualInterface(ManualMenuComponent()))
            ScreenController.activateScene("recoupInterface")
        }

        recoupScanButton.onAction = javafx.event.EventHandler {
            //ITSMFunctions.sortTicketsBasedOnStatus()
            ScreenController.addScene("recoupScan", ScanInterface())
            ScreenController.activateScene("recoupScan")
        }

        macButton.onAction = javafx.event.EventHandler {

            DriverFunctions.switchToTab("itsm")
            val date = Date()
            val formatter = SimpleDateFormat("dd-MMM-yy")
            val strDate = if (formatter.format(date).contains("."))
                formatter.format(date).replace(".", "")
            else
                formatter.format(date)
            println(strDate)
        }
        return rootContainer
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/task_select.fxml"))
        fxmlLoader.classLoader = javaClass.classLoader
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
            exception.printStackTrace()
        }
        interfaceRoot.children.add(configureRootContainer())
    }

}