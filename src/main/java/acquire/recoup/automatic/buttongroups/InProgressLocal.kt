package acquire.recoup.automatic.buttongroups

import acquire.recoup.automatic.EmailInterface
import acquire.recoup.automatic.buttons.AddNoteButton
import acquire.recoup.automatic.buttons.CreateEmailFromTemplateButton
import acquire.recoup.automatic.buttons.CreateWaybillButton
import acquire.recoup.automatic.buttons.SendNudgeButton
import acquire.recoup.manual.ManualActionTypes
import javafx.geometry.Pos
import javafx.scene.layout.VBox

class InProgressLocal(
        private val emailInterface: EmailInterface
) : VBox() {

    private val emailButtonFromTemplate = CreateEmailFromTemplateButton(emailInterface)
    private val waybillButton = CreateWaybillButton()
    private val noteButton = AddNoteButton()

    init {
        emailButtonFromTemplate.text = "Send Reminder"
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(emailButtonFromTemplate, waybillButton, noteButton)
    }

}