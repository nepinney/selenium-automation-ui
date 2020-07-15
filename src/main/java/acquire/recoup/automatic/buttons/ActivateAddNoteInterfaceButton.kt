package acquire.recoup.automatic.buttons

import acquire.ScreenController
import javafx.scene.control.Button

class ActivateAddNoteInterfaceButton : Button("Add a note") {

    init {
        this.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("addNoteInterface")
        }
    }
}