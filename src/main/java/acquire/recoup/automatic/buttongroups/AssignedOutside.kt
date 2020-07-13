package acquire.recoup.automatic.buttongroups

import acquire.recoup.automatic.buttons.AddNoteButton
import acquire.recoup.automatic.buttons.CreateRequestEmailButton
import javafx.geometry.Pos
import javafx.scene.layout.VBox

class AssignedOutside : VBox() {

    private val requestEmailButton = CreateRequestEmailButton()
    private val addNoteButton = AddNoteButton()

    init {
        this.spacing = 5.0
        this.alignment = Pos.CENTER
        this.children.addAll(requestEmailButton, addNoteButton)
    }

}