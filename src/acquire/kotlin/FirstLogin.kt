package acquire.kotlin

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import java.io.File
import java.io.IOException

/**
 * @Author Nicholas Pinney
 */
class FirstLogin internal constructor() : GridPane() {

    @FXML private lateinit var usernameField: TextField
    @FXML private lateinit var passwordField: PasswordField
    @FXML private lateinit var pinFieldOne: PasswordField
    @FXML private lateinit var pinFieldTwo: PasswordField
    @FXML private lateinit var statusMessage: Label
    @FXML private lateinit var pinGrid: GridPane
    @FXML private lateinit var setPinButton: Button
    @FXML private lateinit var loginButton: Button

    private val file = File(Config.readPathToCredFile())

    private var username: String
        get() = usernameProperty().get()
        set(value) {
            usernameProperty().set(value)
        }

    private fun usernameProperty(): StringProperty {
        return usernameField.textProperty()
    }

    private var password: String
        get() = passwordProperty().get()
        set(value) {
            passwordProperty().set(value)
        }

    private fun passwordProperty(): StringProperty {
        return passwordField.textProperty()
    }

    private fun showMessage(fieldMissing: String, error: Boolean) {
        if (!statusMessage.isVisible) statusMessage.isVisible = true
        if (error) {
            statusMessage.text = fieldMissing
            statusMessage.textFill = Color.RED
        } else {
            statusMessage.text = "Pin Set If Correct Login - Logging in..."
            //TODO: Log in and check if credentials are valid before creating file and moving to dashboard
            statusMessage.textFill = Color.DARKORANGE
            setPinButton.visibleProperty().set(false)
            loginButton.visibleProperty().set(false)
            usernameField.isEditable = false
            passwordField.isEditable = false
            pinFieldOne.isEditable = false
            pinFieldTwo.isEditable = false
        }
    }

    @Throws(EmptyFieldException::class)
    private fun checkFields() {
       //when (username) { "" -> throw acquire.kotlin.EmptyFieldException("Enter Username") }
        if (username.isEmpty()) throw EmptyFieldException("Enter Username")
        if (password.isBlank()) throw EmptyFieldException("Enter Password")
        if (pinFieldOne.text.isBlank()) throw EmptyFieldException("Enter Pin")
        if (pinFieldTwo.text.isBlank()) throw EmptyFieldException("Re-Enter Pin")
    }

    private fun login(u: String, p: String) {
        SeleniumMethods.login(u, p)
    }

    @FXML
    fun login() {
        try {//TODO: Make a seperate check for only username and password because it will ask for pin still
            checkFields()
            login(usernameField.text, passwordField.text)
        } catch (e: EmptyFieldException) {
            showMessage(e.toString(), true)
        }
    }

    @Throws(PinFieldsNotMatchingException::class)
    private fun checkPinAccuracy(): Boolean {
        return if (pinFieldOne.text == pinFieldTwo.text) true else {
            pinFieldOne.text = ""
            pinFieldTwo.text = ""
            throw PinFieldsNotMatchingException()
        }
    }

    @FXML
    fun setPin() {
        var credentialsCreated: Boolean
        try {
            checkFields()
            checkPinAccuracy()
            credentialsCreated = CredentialManager(
                pinFieldOne.text,
                usernameField.text,
                passwordField.text
            ).createCredentialFile()
            println("Cred file created: $credentialsCreated")
            showMessage("", false)
            login(usernameField.text, passwordField.text)
        } catch (e: EmptyFieldException) {
            showMessage(e.toString(), true)
        } catch (e: PinFieldsNotMatchingException) {
            showMessage(e.toString(), true)
        }

    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/first_login.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}