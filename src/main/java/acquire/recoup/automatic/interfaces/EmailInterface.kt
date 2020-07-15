package acquire.recoup.automatic.interfaces

import acquire.*
import acquire.recoup.automatic.AddNote
import acquire.recoup.automatic.ButtonGroups
import acquire.recoup.automatic.NoteType
import acquire.recoup.components.TicketModel
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

class EmailInterface(
        private val ticketModel: TicketModel
) : BorderPane() {

    private val emailMetaDataGrid = GridPane()
    private val buttonsBox = HBox()

    private val backButton = BackButton("recoupScan")

    private val recipientField = TextField("Recipient(s)")
    private val ccField = TextField("CC")
    private val subjectField = TextField("Subject")
    private val bodyArea = TextArea("Email Body")
    private val sendButton = Button("Send & Add to notes")

    private var email: Email? = null

    private fun configureEmailMetaDataGrid() {
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
        emailMetaDataGrid.columnConstraints.addAll(col1, col2)
        emailMetaDataGrid.vgap = 5.0

        emailMetaDataGrid.add(toLabel, 0, 0)
        emailMetaDataGrid.add(recipientField, 1, 0)

        emailMetaDataGrid.add(ccLabel, 0, 1)
        emailMetaDataGrid.add(ccField, 1, 1)

        emailMetaDataGrid.add(subjectLabel, 0, 2)
        emailMetaDataGrid.add(subjectField, 1, 2)

    }

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(sendButton, backButton)

        sendButton.onAction = javafx.event.EventHandler {
            email = Email(
                    CreateEmail.determineRecipients(ticketModel).first(),
                    CreateEmail.determineRecipients(ticketModel).last(),
                    subjectField.text,
                    bodyArea.text)
            CreateEmail.buildEmail(email!!)
            CreateEmail.launchOutlook()
            AddNote.processNote(email, null, NoteType.EMAILNOTE)
            ScreenController.activateScene("recoupScan")
        }
    }

    private fun configureThis() {
        this.top = emailMetaDataGrid
        BorderPane.setMargin(emailMetaDataGrid, Insets(5.0, 5.0, 0.0, 5.0))
        this.center = bodyArea
        BorderPane.setMargin(bodyArea, Insets(5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 0.0, 5.0, 5.0))
    }

    fun populateFieldsWithCurrentTicket() {
        val recipientsLists = CreateEmail.determineRecipients(ticketModel)
        val recipients = recipientsLists.first().joinToString()
        val ccs = if (recipientsLists.lastIndex == 1)
            recipientsLists.last().joinToString()
        else
            ""
        val bodyAndSubject = when (ticketModel.currentTicket.value.automaticButtonGroup) {
            ButtonGroups.ASSIGNEDLOCAL -> {
                CreateEmail.generateEmailSubjectAndBodyFromTemplate(Config.readLocalRequestEmailLocation(), ticketModel)
            }
            ButtonGroups.ASSIGNEDOUTSIDE -> {
                CreateEmail.generateEmailSubjectAndBodyFromTemplate(Config.readOutsideRequestEmailLocation(), ticketModel)
            }
            ButtonGroups.INPROGRESSLOCAL -> {
                CreateEmail.generateEmailSubjectAndBodyFromTemplate(Config.readLocalReminderEmailLocation(), ticketModel)
            }
            ButtonGroups.INPROGRESSOUTSIDE -> {
                CreateEmail.generateEmailSubjectAndBodyFromTemplate(Config.readOutsideReminderEmailLocation(), ticketModel)
            }
            else -> {
                listOf("Failed reading email")
            }
        }
        recipientField.text = recipients
        ccField.text = ccs
        subjectField.text = bodyAndSubject.first()
        bodyArea.text = bodyAndSubject.last()
    }

    init {
        configureEmailMetaDataGrid()
        configureButtonsBox()
        configureThis()
    }

}