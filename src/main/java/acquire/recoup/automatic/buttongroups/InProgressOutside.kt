package acquire.recoup.automatic.buttongroups

import acquire.recoup.automatic.EmailInterface
import acquire.recoup.automatic.buttons.*
import javafx.geometry.Pos
import javafx.scene.layout.VBox

class InProgressOutside(
        private val emailInterface: EmailInterface
) : VBox() {

    private val emailButtonFromTemplate = CreateEmailFromTemplateButton(emailInterface)
    private val waybillButton = CreateWaybillButton()
    private val noteButton = AddNoteButton()
    private val puroStatusButton = PurolatorStatusButton()

    init {
        emailButtonFromTemplate.text = "Send Reminder"
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(emailButtonFromTemplate, puroStatusButton, waybillButton, noteButton)
    }
}