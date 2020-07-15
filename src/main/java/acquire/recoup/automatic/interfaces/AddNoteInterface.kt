package acquire.recoup.automatic.interfaces

import acquire.recoup.automatic.buttons.AddNoteButton
import javafx.geometry.HPos
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane

class AddNoteInterface : GridPane() {

    private val notesField = TextField()
    private val addNotesBtn = AddNoteButton()

    init {
        GridPane.setHalignment(addNotesBtn, HPos.RIGHT)
        this.add(notesField, 0, 0)
        this.add(addNotesBtn, 0, 1)
    }

}