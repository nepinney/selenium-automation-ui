package acquire.recoup.automatic.buttons

import acquire.ScreenController
import acquire.recoup.automatic.EmailInterface
import acquire.recoup.manual.ManualActionTypes
import javafx.scene.control.Button
import javafx.stage.Screen

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