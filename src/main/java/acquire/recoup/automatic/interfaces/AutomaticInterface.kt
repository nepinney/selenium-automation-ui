package acquire.recoup.automatic.interfaces

import acquire.BackButton
import acquire.SceneHandle
import acquire.ScreenController
import acquire.Ticket
import acquire.recoup.RecoupNotesParser
import acquire.recoup.TicketModel
import acquire.recoup.automatic.AutoLabel
import acquire.recoup.automatic.NotesInstructionsRadio
import acquire.recoup.automatic.TicketDisplayType
import acquire.ListenerHandle
import acquire.recoup.automatic.buttongroups.*
import acquire.recoup.automatic.buttons.StartButton
import acquire.recoup.components.PrintTicketButton
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

class AutomaticInterface : BorderPane(), ListenerHandle, SceneHandle {

    //Panes
    private val buttonsBox = HBox()
    private val labelsGrid = GridPane()
    private val noteAndButtonsGrid = GridPane()
    private val radioOptions = NotesInstructionsRadio()

    //Global elements
    private val note = Text("")
    //Labels
    private val tsc = AutoLabel("")
    private val status = Label("")
    private val lastUpdated = Label("")
    private val type = Label("")

    //Listeners
    private val onRadioChangeListener = ChangeListener<Toggle> { observableValue: ObservableValue<out Toggle>, oldTicketDisplayType: Toggle?, newTicketDisplayType: Toggle ->
        when (newTicketDisplayType.userData == TicketDisplayType.INSTRUCTIONS) {
            true -> {
                note.text = "Client to update and validate: \n${RecoupNotesParser.clientToUpdateAndValidate(TicketModel.currentTicket.value.notes)}\n\n" +
                        "Instructions: \n${RecoupNotesParser.exactLocation(TicketModel.currentTicket.value.notes)}\n\n" +
                        "Quantity: \n${RecoupNotesParser.quantityInstructions(TicketModel.currentTicket.value.notes)}\n\n"
            }
            false -> {
                note.text = TicketModel.currentTicket.value.lastNote
            }
        }
    }
    private val listenerToUpdateLatestNote = ChangeListener<Ticket>() { observableValue: ObservableValue<out Ticket>?, oldTicket: Ticket?, newTicket: Ticket? ->
        when (observableValue?.value?.lastNote == "NO NOTE") {
            true -> {
                radioOptions.selection.selectToggle(radioOptions.showNote)
                radioOptions.selection.selectToggle(radioOptions.showInstruction)
            }
            false -> {
                note.text = observableValue?.value?.lastNote
                radioOptions.selection.selectToggle(radioOptions.showNote)
            }
        }
        //note.text = p0.value.lastNote
    }
    private val listenerToUpdateButtonGroup = ChangeListener<Ticket>() { observableValue: ObservableValue<out Ticket>, oldTicket: Ticket?, newTicket: Ticket? ->
        if (oldTicket != null) {
            when (oldTicket?.automaticButtonGroup) {
                ButtonGroups.ASSIGNEDLOCAL -> {
                    noteAndButtonsGrid.children.remove(assignedLocal)
                }
                ButtonGroups.ASSIGNEDOUTSIDE -> {
                    noteAndButtonsGrid.children.remove(assignedOutside)
                }
                ButtonGroups.INPROGRESSLOCAL -> {
                    noteAndButtonsGrid.children.remove(inProgressLocal)
                }
                ButtonGroups.INPROGRESSOUTSIDE -> {
                    noteAndButtonsGrid.children.remove(inProgressOutside)
                }
            }
        }
        when (newTicket?.automaticButtonGroup) {
            ButtonGroups.ASSIGNEDLOCAL -> { selectAssignedLocalGroup() }
            ButtonGroups.ASSIGNEDOUTSIDE -> { selectAssignedOutsideGroup() }
            ButtonGroups.INPROGRESSLOCAL -> { selectInProgressLocalGroup() }
            ButtonGroups.INPROGRESSOUTSIDE -> { selectInProgressOutsideGroup() }
        }
    }
    private val listenerToUpdateLabels = ChangeListener<Ticket>() { observableValue: ObservableValue<out Ticket>, oldTicket: Ticket?, newTicket: Ticket? ->
        println("Ticket changed")
        tsc.text = RecoupNotesParser.tscNumber(observableValue.value.notes)
        status.text = observableValue.value.status
        lastUpdated.text = observableValue.value.lastModified?.takeWhile { it != ' ' }
        type.text = observableValue.value.ticketType
    }

    //Interface reference
    private val emailInterface = EmailInterface()
    private val instructionScreen = AutomaticInstructionsInterface()
    private val addressInterface = AddressInterface()
    private val addNoteInterface = AddNoteInterface()


    //Buttons
    private val startBtn = StartButton()
    private val backButton = BackButton("taskSelectionScene",
            listOf(this),
            listOf(this, addressInterface),
            resetTicket = true)
    private val endBtn = Button("End")
    private val refreshButton = Button("Refresh")

    private val instructionsBtn = Button("Instructions")

    //Button Groups
    private val assignedLocal = AssignedLocal()
    private val assignedOutside = AssignedOutside()
    private val inProgressLocal = InProgressLocal()
    private val inProgressOutside = InProgressOutside()

    override fun activateListeners() {
        //println("Activate listeners was called in AutomaticInterface")
        TicketModel.currentTicket.addListener(listenerToUpdateLabels)
        radioOptions.selection.selectedToggleProperty().addListener(onRadioChangeListener)
        TicketModel.currentTicket.addListener(listenerToUpdateLatestNote)
        TicketModel.currentTicket.addListener(listenerToUpdateButtonGroup)
    }

    override fun deactivateListeners() {
        TicketModel.currentTicket.removeListener(listenerToUpdateLabels)
        radioOptions.selection.selectedToggleProperty().removeListener(onRadioChangeListener)
        TicketModel.currentTicket.removeListener(listenerToUpdateLatestNote)
        TicketModel.currentTicket.removeListener(listenerToUpdateButtonGroup)
    }

    override fun addChildScenes() {
        ScreenController.addScene("emailInterface", emailInterface)
        ScreenController.addScene("scanInstructions", instructionScreen)
        ScreenController.addScene("addressInterface", addressInterface)
        ScreenController.addScene("addNoteInterface", addNoteInterface)
    }

    override fun removeChildScenes() {
        ScreenController.removeScene("emailInterface")
        ScreenController.removeScene("scanInstructions")
        ScreenController.removeScene("addressInterface")
        ScreenController.removeScene("addNoteInterface")
        ScreenController.removeScene("recoupScan")
    }

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
        val statusLabel = Label("Status: ")
        val lastUpdatedLabel = Label("Note date: ")
        val typeLabel = Label("Type: ")


        val textItems = listOf(tscLabel, Label(), statusLabel, status, lastUpdatedLabel, lastUpdated, typeLabel, type)
        textItems.forEach{ it.style = "-fx-font-size: 150%;" }
        textItems.filter { textItems.indexOf(it) % 2 == 1 }
                .forEach { it.style = "-fx-font-weight: bold; -fx-font-size: 150%;" }

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
        val noteAndRadioGrid = GridPane()
        val viewNote = TextFlow(note)
        val scrollerPane = ScrollPane(viewNote)
        scrollerPane.style = "-fx-background-color:transparent;";
        noteAndRadioGrid.add(scrollerPane, 0, 0)
        noteAndRadioGrid.add(radioOptions, 0, 1)

        viewNote.maxWidthProperty().bind(scrollerPane.widthProperty())

        val mainCol = ColumnConstraints()
        mainCol.percentWidth = 100.0
        val row12 = RowConstraints()
        row12.percentHeight = 90.0
        val row2 = RowConstraints()
        row2.percentHeight = 10.0
        noteAndRadioGrid.columnConstraints.addAll(mainCol)
        noteAndRadioGrid.rowConstraints.addAll(row12, row2)

        GridPane.setValignment(viewNote, VPos.CENTER)
        GridPane.setHalignment(viewNote, HPos.CENTER)
        noteAndButtonsGrid.add(noteAndRadioGrid, 0, 0)
        //noteAndButtonsGrid.add(NotesInstructionsRadio(), 0, 1)

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
            TicketModel.currentTicket.value.lastNote = ITSMFunctions.getTicketsLastNote(=-        }*/
    }

    init {
        TicketModel.setCurrentTicket(Ticket("", ""))

        //Configure instructions button
        instructionsBtn.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("scanInstructions")
        }

        configureButtonsBox()
        configureLabelsGrid()
        configureNoteAndButtonsGrid()

        //Button groups change depending on the type of ticket
            //Type of the ticket is stored in the ticket itself and gets set in ITSM functions when the ticket is fetched
        configureThis()
    }


}

