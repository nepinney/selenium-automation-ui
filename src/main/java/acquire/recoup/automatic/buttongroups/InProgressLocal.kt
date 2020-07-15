package acquire.recoup.automatic.buttongroups

import acquire.ScreenController
import acquire.recoup.automatic.interfaces.EmailInterface
import acquire.recoup.automatic.buttons.AddNoteButton
import acquire.recoup.automatic.buttons.CreateEmailFromTemplateButton
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class InProgressLocal(
        private val emailInterface: EmailInterface
) : VBox() {

    private val emailButtonFromTemplate = CreateEmailFromTemplateButton(emailInterface)
    private val waybillButton = Button("Create Waybill")
    private val noteButton = AddNoteButton()

    init {
        emailButtonFromTemplate.text = "Send Reminder"
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(emailButtonFromTemplate, waybillButton, noteButton)

        waybillButton.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("addressInterface")
        }

    }

}