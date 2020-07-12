package acquire

import javafx.scene.control.Button

class BackButton(
        private val previousScreen: String
) : Button("Back") {

    init {
        this.onAction = javafx.event.EventHandler {
            ScreenController.activateScene(previousScreen)
        }
    }

}