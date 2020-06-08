package acquire.kotlin

import acquire.kotlin.recoup.RecoupInterface
import acquire.kotlin.recoup.action.LocalRequest
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import java.io.IOException

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
            ScreenController.addScene("recoupInterface", RecoupInterface(MenuFrame()))
            ScreenController.activateScene("recoupInterface")
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