package acquire.login

import acquire.ITSMFunctions
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.PasswordField
import javafx.scene.layout.GridPane
import java.io.IOException

/**
 * @Author Nicholas Pinney
 */
class PinLogin internal constructor() : GridPane() {

    @FXML private lateinit var pinField: PasswordField

    @FXML
    fun checkPin() {
        try {
            val credentials = CredentialManager.decryptCredentialFile(pinField.text)
            ITSMFunctions.login(credentials[1], credentials[2])
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/pin_login.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}