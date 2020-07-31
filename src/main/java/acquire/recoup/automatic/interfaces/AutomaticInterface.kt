package acquire.recoup.automatic.interfaces

import acquire.*
import acquire.recoup.RecoupNotesParser
import acquire.recoup.automatic.AutoLabel
import acquire.recoup.automatic.buttongroups.ButtonGroups
import acquire.recoup.automatic.buttongroups.AssignedLocal
import acquire.recoup.automatic.buttongroups.AssignedOutside
import acquire.recoup.automatic.buttongroups.InProgressLocal
import acquire.recoup.automatic.buttongroups.InProgressOutside
import acquire.recoup.automatic.buttons.StartButton
import acquire.recoup.components.PrintTicketButton
import acquire.recoup.TicketModel
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

class AutomaticInterface : BorderPane() {

    //Panes
    private val buttonsBox = HBox()
    private val labelsGrid = GridPane()
    private val noteAndButtonsGrid = GridPane()

    //Buttons
    private val startBtn = StartButton()
    private val backButton = BackButton("taskSelectionScene")
    private val endBtn = Button("End")
    private val refreshButton = Button("Refresh")
    private val instructionsBtn = Button("Instructions")
    private val instructionScreen = AutomaticInstructionsInterface()

    //Interface reference
    private val emailInterface = EmailInterface()
    private val addressInterface = AddressInterface()
    private val addNoteInterface = AddNoteInterface()

    //Button Groups
    private val assignedLocal = AssignedLocal(emailInterface)
    private val assignedOutside = AssignedOutside(emailInterface)
    private val inProgressLocal = InProgressLocal(emailInterface)
    private val inProgressOutside = InProgressOutside(emailInterface)

    private fun selectAssignedLocalGroup() {
        noteAndButtonsGrid.add(assignedLocal, 1, 0)
    }

    private fun selectAssignedOutsideGroup() {
        noteAndButtonsGrid.add(assignedOutside, 1, 0)
    }

    private fun selectInProgressLocalGroup() {
        noteAndButtonsGrid.add(inProgressLocal, 1, 0)
    }

    private fun selectInProgressOutsideGroup() {
        noteAndButtonsGrid.add(inProgressOutside, 1, 0)
    }

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(startBtn, PrintTicketButton(TicketModel.currentTicket), endBtn, refreshButton, instructionsBtn, backButton)
    }

    private fun configureLabelsGrid() {
        val tscLabel = Label("TSC #: ")
        val tsc = AutoLabel("")
        val statusLabel = Label("Status: ")
        val status = Label("")
        val lastUpdatedLabel = Label("Note date: ")
        val lastUpdated = Label("")
        val typeLabel = Label("Type: ")
        val type = Label("")

        val textItems = listOf(tscLabel, Label(), statusLabel, status, lastUpdatedLabel, lastUpdated, typeLabel, type)
        textItems.forEach{ it.style = "-fx-font-size: 150%;" }
        textItems.filter { textItems.indexOf(it) % 2 == 1 }
                .forEach { it.style = "-fx-font-weight: bold; -fx-font-size: 150%;" }

        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            tsc.text = RecoupNotesParser.tscNumber(p0.value.notes)
            status.text = p0.value.status
            lastUpdated.text = p0.value.lastModified?.takeWhile { it != ' ' }
            type.text = p0.value.ticketType
        }

        labelsGrid.add(tscLabel, 0, 0)
        labelsGrid.add(tsc, 1, 0)
        labelsGrid.add(statusLabel, 2, 0)
        labelsGrid.add(status, 3, 0)
        labelsGrid.add(typeLabel, 0, 1)
        labelsGrid.add(type, 1, 1)
        labelsGrid.add(lastUpdatedLabel, 2, 1)
        labelsGrid.add(lastUpdated, 3, 1)

        val col1 = ColumnConstraints()
        col1.percentWidth = 14.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 34.0
        val col3 = ColumnConstraints()
        col3.percentWidth = 20.0
        val col4 = ColumnConstraints()
        col4.percentWidth = 32.0
        labelsGrid.columnConstraints.addAll(col1, col2, col3, col4)
    }

    private fun configureNoteAndButtonsGrid() {
        val note = Text("")
        val viewNote = TextFlow(note)

        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            note.text = p0.value.lastNote
        }
        GridPane.setValignment(viewNote, VPos.CENTER)
        GridPane.setHalignment(viewNote, HPos.CENTER)
        noteAndButtonsGrid.add(viewNote, 0, 0)

        GridPane.setValignment(assignedLocal, VPos.CENTER)
        GridPane.setValignment(assignedOutside, VPos.CENTER)
        GridPane.setValignment(inProgressLocal, VPos.CENTER)
        GridPane.setValignment(inProgressOutside, VPos.CENTER)

        val col1 = ColumnConstraints()
        col1.percentWidth = 48.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 52.0
        noteAndButtonsGrid.columnConstraints.addAll(col1, col2)

        val row1 = RowConstraints()
        row1.percentHeight = 100.0
        noteAndButtonsGrid.rowConstraints.addAll(row1)
    }

    private fun configureThis() {
        this.top = labelsGrid
        this.center = noteAndButtonsGrid
        BorderPane.setMargin(noteAndButtonsGrid, Insets(5.0, 0.0, 0.0, 0.0))
        this.bottom = buttonsBox
        this.style = "-fx-padding: 5;"

        /*refreshButton.onAction = javafx.event.EventHandler {
            ticketModel.currentTicket.value.lastNote = ITSMFunctions.getTicketsLastNote(=-        }*/
    }

    init {

        ScreenController.addScene("emailInterface", emailInterface)
        ScreenController.addScene("scanInstructions", instructionScreen)
        ScreenController.addScene("addressInterface", addressInterface)
        ScreenController.addScene("addNoteInterface", addNoteInterface)

        //Configure instructions button
        instructionsBtn.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("scanInstructions")
        }

        ticketModel.setCurrentTicket(Ticket("", "", ticketIndex = 2))

        configureButtonsBox()
        configureLabelsGrid()
        configureNoteAndButtonsGrid()

        //Button groups change depending on the type of ticket
            //Type of the ticket is stored in the ticket itself and gets set in ITSM functions when the ticket is fetched
        ticketModel.currentTicket.addListener { p0, oldValue, newValue ->
            when (oldValue.automaticButtonGroup) {
                ButtonGroups.ASSIGNEDLOCAL -> { noteAndButtonsGrid.children.remove(assignedLocal) }
                ButtonGroups.ASSIGNEDOUTSIDE -> { noteAndButtonsGrid.children.remove(assignedOutside) }
                ButtonGroups.INPROGRESSLOCAL -> { noteAndButtonsGrid.children.remove(inProgressLocal) }
                ButtonGroups.INPROGRESSOUTSIDE -> { noteAndButtonsGrid.children.remove(inProgressOutside) }
            }
            when (newValue.automaticButtonGroup) {
                ButtonGroups.ASSIGNEDLOCAL -> { selectAssignedLocalGroup() }
                ButtonGroups.ASSIGNEDOUTSIDE -> { selectAssignedOutsideGroup() }
                ButtonGroups.INPROGRESSLOCAL -> { selectInProgressLocalGroup() }
                ButtonGroups.INPROGRESSOUTSIDE -> { selectInProgressOutsideGroup() }
            }
        }
        configureThis()
    }
}

