package acquire.recoup.automatic.buttongroups

import acquire.ScreenController
import acquire.recoup.automatic.buttons.ActivateAddNoteInterfaceButton
import acquire.recoup.automatic.buttons.CreateEmailFromTemplateButton
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class InProgressLocal : VBox() {

    private val emailButtonFromTemplate = CreateEmailFromTemplateButton()
    private val waybillButton = Button("Create Waybill")
    private val noteButton = ActivateAddNoteInterfaceButton()

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