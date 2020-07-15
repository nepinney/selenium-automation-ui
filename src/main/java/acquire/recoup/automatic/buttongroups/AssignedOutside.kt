package acquire.recoup.automatic.buttongroups

import acquire.recoup.automatic.interfaces.EmailInterface
import acquire.recoup.automatic.buttons.AddNoteButton
import acquire.recoup.automatic.buttons.CreateEmailFromTemplateButton
import javafx.geometry.Pos
import javafx.scene.layout.VBox

class AssignedOutside(
        private val emailInterface: EmailInterface
) : VBox() {

    private val requestEmailButton = CreateEmailFromTemplateButton(emailInterface)
    private val addNoteButton = AddNoteButton()

    init {
        requestEmailButton.text = "Create First Request Email"
        addNoteButton.onAction = javafx.event.EventHandler {
            println("Assigned Outside")
        }

        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(requestEmailButton, addNoteButton)
    }

}