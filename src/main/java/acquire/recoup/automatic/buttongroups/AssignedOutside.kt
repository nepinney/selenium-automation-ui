package acquire.recoup.automatic.buttongroups

import acquire.ScreenController
import acquire.recoup.automatic.buttons.ActivateAddNoteInterfaceButton
import acquire.recoup.automatic.interfaces.EmailInterface
import acquire.recoup.automatic.buttons.CreateEmailFromTemplateButton
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class AssignedOutside(
        private val emailInterface: EmailInterface
) : VBox() {

    private val requestEmailButton = CreateEmailFromTemplateButton(emailInterface)
    private val addNoteButton = ActivateAddNoteInterfaceButton()

    init {
        requestEmailButton.text = "Create First Request Email"
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(requestEmailButton, addNoteButton)
    }

}