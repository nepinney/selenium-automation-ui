package acquire.recoup.automatic

import javafx.scene.control.Label
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

class AutoLabel(str: String) : Label(str) {

    init {
        this.style = "-fx-font-weight: bold; -fx-font-size: 150%;"
        this.onMouseClicked = javafx.event.EventHandler {
            when (true) {
                this.text.isBlank() -> { }
                true -> {
                    val clipboard = Clipboard.getSystemClipboard()
                    val content = ClipboardContent()
                    content.putString(this.text)
                    clipboard.setContent(content)
                    this.style = "-fx-font-size: 150%; -fx-focus-color: green ;"
                }
            }
        }
    }

}