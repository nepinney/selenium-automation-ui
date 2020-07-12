package acquire.recoup.manual

import acquire.ScreenController
import javafx.scene.control.Button

class ManuallnstructionsButton : Button("Instructions") {

    init {
        this.onAction = javafx.event.EventHandler {
            ScreenController.addScene("manualInstructions", ManualInstructionInterface())
            ScreenController.activateScene("manualInstructions")
        }
    }
}