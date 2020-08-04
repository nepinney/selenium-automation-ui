package acquire.recoup.automatic.interfaces

import acquire.BackButton
import acquire.ITSMFunctions
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox

class AddNoteInterface : BorderPane() {

    private val buttonsBox = HBox()

    private val notesField = TextArea()
    private val addNotesBtn = Button("Add note to ticket")

    private val backBtn = BackButton("recoupScan")

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(addNotesBtn, backBtn)
    }

    init {

        addNotesBtn.onAction = javafx.event.EventHandler {
            //TODO: Add whatever in textArea to note via itsmFunctions
            ITSMFunctions.addNotesToWorkInfoTextArea(notesField.text)
            ITSMFunctions.saveNewWorkInfo()
            notesField.text = ""
        }

        configureButtonsBox()
        val lb = Label("Add content of box to notes:")
        this.top = lb
        BorderPane.setMargin(lb, Insets(5.0))
        this.center = notesField
        BorderPane.setMargin(notesField, Insets(0.0, 5.0, 5.0, 5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 5.0, 5.0, 5.0))
    }

}