package acquire.recoup.automatic.buttons

import acquire.Email
import acquire.ScreenController
import acquire.recoup.automatic.buttongroups.ButtonGroups
import acquire.recoup.automatic.interfaces.EmailInterface
import javafx.scene.control.Button

class CreateEmailFromTemplateButton : Button(){

    init {
        this.onAction = javafx.event.EventHandler {
            //TODO: Create new email interface
            val eInterface = EmailInterface()

            //TODO: Create fucking email
            //All emails are based off of the current ticket
            val newEmail = Email()
            newEmail.buildViaBGroup()

            //TODO: Pass email to email interface
            eInterface.populateFields(newEmail)

            //TODO: Add email interface to ScreenController
            ScreenController.addScene("emailInterface", eInterface)

            //TODO: Activate email interface
            ScreenController.activateScene("emailInterface")

        }
    }

}