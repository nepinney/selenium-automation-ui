package acquire.kotlin

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
            //System.out.println("Username: " + decryptedUsername);
            //System.out.println("Password: " + decryptedPassword);
            //login(decryptedUsername, decryptedPassword)
            SeleniumMethods.login(credentials[1], credentials[2])
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //System.out.println("Does pin match records?: " + goodPin);


        /*if (encryptedPin == null) {
            try {
                File file = new File("Cred.txt");
                Scanner scanner = new Scanner(file);
                getEncryptedPin(scanner);
                System.out.println(encryptedPin);
                String decryptedPin = decryptPin(encryptedPin, pinField.getText());
                System.out.println("Does pin match records?: " + decryptedPin);
                //this.setUsername(scanner.next());
                //this.setPassword(scanner.next());
            }
            catch (FileNotFoundException e) {
                //this.credentialsExist = false;
                System.out.println("No credentials exist");
            }
        }*/
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/pin_login.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}