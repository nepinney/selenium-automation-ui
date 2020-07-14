package acquire.recoup.automatic

import acquire.BackButton
import acquire.Config
import acquire.CreateEmail
import acquire.Email
import acquire.recoup.components.TicketModel
import acquire.recoup.manual.ManualActionTypes
import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane

class EmailInterface(
        private val ticketModel: TicketModel
) : GridPane() {

    private val backButton = BackButton("recoupScan")

    private val recipientField = TextField("Recipient(s)")
    private val ccField = TextField("CC")
    private val subjectField = TextField("Subject")
    private val bodyArea = TextArea("Email Body")
    private val sendButton = Button("Send & add to notes")

    private var email: Email? = null

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
        this.vgap = 5.0

        GridPane.setHalignment(sendButton, HPos.RIGHT)

        this.add(recipientField, 0, 0)
        this.add(ccField, 0, 1)
        this.add(subjectField, 0, 2)
        this.add(bodyArea, 0, 3)
        this.add(sendButton, 0, 4)
        this.add(backButton, 0, 5)

        sendButton.onAction = javafx.event.EventHandler {
            email = Email(
                    CreateEmail.determineRecipients(ticketModel).first(),
                    CreateEmail.determineRecipients(ticketModel).last(),
                    subjectField.text,
                    bodyArea.text)
            CreateEmail.buildEmail(email!!)
            CreateEmail.launchOutlook()
            AddNote.processNote(email, null, NoteType.EMAILNOTE)
        }

/*        if (loadEmail != null) {
            populateFields()
        }*/

    }

}