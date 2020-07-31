package acquire.recoup.automatic.interfaces

import acquire.*
import acquire.recoup.automatic.buttongroups.ButtonGroups
import acquire.recoup.automatic.NoteType
import acquire.recoup.TicketModel
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import java.text.SimpleDateFormat
import java.util.*

class EmailInterface : BorderPane() {
    private val labelsGrid = GridPane()
    private val buttonsBox = HBox()

    private val backButton = BackButton("recoupScan")

    private val recipientField = TextField("Recipient(s)")
    private val ccField = TextField("CC")
    private val subjectField = TextField("Subject")
    private val bodyArea = TextArea("Email Body")
    private val sendButton = Button("Send & Add to notes")

    private var email: Email? = null

    private fun configureLabelsGrid() {
        val toLabel = Label("To: ")
        val ccLabel = Label("Cc: ")
        val subjectLabel = Label("Subject: ")

        GridPane.setHalignment(toLabel, HPos.RIGHT)
        GridPane.setHalignment(ccLabel, HPos.RIGHT)
        GridPane.setHalignment(subjectLabel, HPos.RIGHT)

        val col1 = ColumnConstraints()
        col1.percentWidth = 10.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 90.0
        labelsGrid.columnConstraints.addAll(col1, col2)
        labelsGrid.vgap = 5.0

        labelsGrid.add(toLabel, 0, 0)
        labelsGrid.add(recipientField, 1, 0)

        labelsGrid.add(ccLabel, 0, 1)
        labelsGrid.add(ccField, 1, 1)

        labelsGrid.add(subjectLabel, 0, 2)
        labelsGrid.add(subjectField, 1, 2)

    }

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(sendButton, backButton)

        sendButton.onAction = javafx.event.EventHandler {
            //Set the email variables to the fields of the interface to allow for email 'edit'
            //TODO: email.recipientList =
            //TODO: cc.recipientList =
            email!!.subject = subjectField.text
            email!!.body = bodyArea.text

            email!!.buildEmlFile()
            email!!.launchOutlook()

            //Process the email (add metadata) and add the string to the notes of the ticket
            processEmailAndAddToTicketNotes(email, null, NoteType.EMAILNOTE)

            //Change the status of the ticket if it's assigned
            when (TicketModel.currentTicket.value.automaticButtonGroup) {
                ButtonGroups.INPROGRESSOUTSIDE -> {}
                ButtonGroups.INPROGRESSLOCAL -> {}
                else -> {
                    ITSMFunctions.setStatusField("In Progress")
                }
            }
            ScreenController.activateScene("recoupScan")
            ScreenController.removeScene("emailInterface")
        }
    }

    private fun configureThis() {
        this.top = labelsGrid
        BorderPane.setMargin(labelsGrid, Insets(5.0, 5.0, 0.0, 5.0))
        this.center = bodyArea
        BorderPane.setMargin(bodyArea, Insets(5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 0.0, 5.0, 5.0))
    }

    fun populateFields(email: Email) {
        this.email = email

        recipientField.text = email.recipientList!!.joinToString()
        ccField.text = email.ccList!!.joinToString()
        subjectField.text = email.subject!!
        bodyArea.text = email.body!!
    }

    private fun processEmailAndAddToTicketNotes(email: Email?, note: String?, noteType: NoteType) {
        var noteToAdd: String
        when (noteType) {
            NoteType.EMAILNOTE -> {
                val date = Date()
                val formatter = SimpleDateFormat("MMMM dd, yyyy hh:mm aa")
                val cc = if (email!!.ccList != null) "Cc: ${email!!.ccList?.joinToString(";")}\n" else ""
                noteToAdd =
                        "From: Pinney, Nicholas\n" +
                                "Sent: ${formatter.format(date).toUpperCase().replace(".", "")}\n" +
                                "To: ${email!!.recipientList.joinToString(";")}\n" +
                                "$cc" +
                                "Subject: ${email!!.subject}\n\n" +
                                "${email!!.body}"
            }
            NoteType.CUSTOMNOTE -> {
                noteToAdd = note!!
            }
        }
        //println("\n$noteToAdd")
        ITSMFunctions.setNotes(noteToAdd)
        ITSMFunctions.addWorkInfo()
    }

    init {
        configureLabelsGrid()
        configureButtonsBox()
        configureThis()
    }

}