package acquire.kotlin

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import java.io.IOException

/**
 * @Author Nicholas Pinney
 */
class SplashScene : StackPane() {
    @FXML var statusMessage: Label? = null

    fun getStatusMessage(): String {
        return statusMessageProperty().value
    }

    fun setStatusMessage(value: String) {
        statusMessageProperty().set(value)
        println("at hert")
    }

    fun statusMessageProperty(): StringProperty {
        return statusMessage!!.textProperty()
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/splash_scene.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}