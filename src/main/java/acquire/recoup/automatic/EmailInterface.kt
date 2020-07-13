package acquire.recoup.automatic

import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane

class EmailInterface : GridPane() {

    private val recipientField = TextField("Recipient(s)")
    private val ccField = TextField("CC")
    private val bodyArea = TextArea("Email Body")
    private val sendButton = Button("Send & add to notes")

    init {
        this.vgap = 5.0

        GridPane.setHalignment(sendButton, HPos.RIGHT)

        this.add(recipientField, 0, 0)
        this.add(ccField, 0, 1)
        this.add(bodyArea, 0, 2)
        this.add(sendButton, 0, 3)

    }

}