package acquire.kotlin

import acquire.kotlin.recoup.RecoupInterface
import acquire.kotlin.recoup.RecoupMenuComponent
import acquire.kotlin.recoup.action.LocalRequest
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
        val recoupButton = Button("Recoup")
        val macButton = Button("MAC Provisioning")
        val taskButtons = listOf(recoupButton, macButton)

        AnchorPane.setBottomAnchor(rootContainer, 0.0)
        AnchorPane.setLeftAnchor(rootContainer, 0.0)
        AnchorPane.setRightAnchor(rootContainer, 0.0)
        AnchorPane.setTopAnchor(rootContainer, 0.0)
        rootContainer.spacing = 5.0
        rootContainer.alignment = Pos.CENTER

        rootContainer.children.add(sceneTitle)
        taskButtons.forEach { rootContainer.children.add(it) }

        recoupButton.onAction = javafx.event.EventHandler {
            ScreenController.addScene("recoupInterface", RecoupInterface(RecoupMenuComponent()))
            ScreenController.activateScene("recoupInterface")
        }
        macButton.onAction = javafx.event.EventHandler {
            PurolatorFunctions.openPurolator()
            PurolatorFunctions.login("jpaquete", "eusteam")
            PurolatorFunctions.createShipmentFromHome()
            PurolatorFunctions.enterShippingInformation("Nicholas Pinney","L6H 5T8", "2140", "Winding Woods Drive", "905", "2575603")
            PurolatorFunctions.selectDropOff()
            val date = Date()
            val formatter = SimpleDateFormat("M/dd/yyyy hh:mm:ss aa")
            val strDate = formatter.format(date).replace(".", "").toUpperCase()
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