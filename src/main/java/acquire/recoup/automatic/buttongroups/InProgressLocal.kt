package acquire.recoup.automatic.buttongroups

import acquire.recoup.automatic.buttons.AddNoteButton
import acquire.recoup.automatic.buttons.CreateWaybillButton
import acquire.recoup.automatic.buttons.SendNudgeButton
import javafx.geometry.Pos
import javafx.scene.layout.VBox

class InProgressLocal : VBox() {

    private val nudgeButton = SendNudgeButton()
    private val waybillButton = CreateWaybillButton()
    private val noteButton = AddNoteButton()

    init {
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(nudgeButton, waybillButton, noteButton)
    }

}