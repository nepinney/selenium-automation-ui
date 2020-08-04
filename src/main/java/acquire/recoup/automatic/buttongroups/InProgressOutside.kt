package acquire.recoup.automatic.buttongroups

import acquire.ScreenController
import acquire.recoup.automatic.interfaces.EmailInterface
import acquire.recoup.automatic.buttons.*
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class InProgressOutside : VBox() {

    private val emailButtonFromTemplate = CreateEmailFromTemplateButton()
    private val waybillButton = Button("Create Waybill")
    private val noteButton = ActivateAddNoteInterfaceButton()
    private val puroStatusButton = PurolatorStatusButton()

    init {
        emailButtonFromTemplate.text = "Send Reminder"
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(emailButtonFromTemplate, puroStatusButton, waybillButton, noteButton)

        waybillButton.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("addressInterface")
        }

    }
}