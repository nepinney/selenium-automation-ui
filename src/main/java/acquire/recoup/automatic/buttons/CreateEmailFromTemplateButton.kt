package acquire.recoup.automatic.buttons

import acquire.ScreenController
import acquire.recoup.automatic.interfaces.EmailInterface
import javafx.scene.control.Button

class CreateEmailFromTemplateButton(
        private val emailInterface: EmailInterface
) : Button(){

    init {
        this.onAction = javafx.event.EventHandler {
            emailInterface.populateFieldsWithCurrentTicket()
            ScreenController.activateScene("emailInterface")
        }
    }

}