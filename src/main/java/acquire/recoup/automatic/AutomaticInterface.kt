package acquire.recoup.automatic

import acquire.*
import acquire.exception.TicketIsNullException
import acquire.recoup.RecoupNotesParser
import acquire.recoup.automatic.buttongroups.AssignedLocal
import acquire.recoup.automatic.buttongroups.AssignedOutside
import acquire.recoup.automatic.buttongroups.InProgressLocal
import acquire.recoup.automatic.buttongroups.InProgressOutside
import acquire.recoup.components.TicketModel
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class AutomaticInterface : AnchorPane() {

    private val splitRoot = SplitPane()
    private val bPane = BorderPane()
    private val ticketActionGrid = GridPane()

    private val ticketModel = TicketModel()

    private val startBtn = StartButton(ticketModel)
    private val endBtn = Button("End")
    private val instructionsBtn = Button("Instructions")
    private val instructionScreen = AutomaticInstructions()

    private val assignedLocal = AssignedLocal()
    private val assignedOutside = AssignedOutside()
    private val inProgressLocal = InProgressLocal()
    private val inProgressOutside = InProgressOutside()

    private fun configureHeader(): HBox {
        val box = HBox()
        box.spacing = 5.0
        box.children.addAll(startBtn, endBtn, BackButton("taskSelectionScene"))
        box.alignment = Pos.CENTER
        return box
    }

    private fun configureMainRegionHeader(): GridPane {
        val labelsGrid = GridPane()
        val noteGrid = GridPane()

        val tscLabel = Label("TSC #: ")
        val tsc = AutoLabel("")
        val statusLabel = Label("Status: ")
        val status = Label("")
        val lastUpdatedLabel = Label("Note date: ")
        val lastUpdated = Label("")
        val typeLabel = Label("Type: ")
        val type = Label("")

        //val noteLabel = Label("Note: ")
        //GridPane.setValignment(noteLabel, VPos.TOP)


        val textItems = listOf(tscLabel, Label(), statusLabel, status, lastUpdatedLabel, lastUpdated, typeLabel, type)
        textItems.forEach{ it.style = "-fx-font-size: 150%;" }
        textItems.filter { textItems.indexOf(it) % 2 == 1 }
                 .forEach { it.style = "-fx-font-weight: bold; -fx-font-size: 150%;" }

        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            tsc.text = RecoupNotesParser.tscNumber(p0.value.notes)
            status.text = p0.value.status
            lastUpdated.text = p0.value.lastModified?.takeWhile { it != ' ' }
            type.text = p0.value.ticketType
            //note.text = p0.value.lastNote
        }

        labelsGrid.add(tscLabel, 0, 0)
        labelsGrid.add(tsc, 1, 0)
        labelsGrid.add(statusLabel, 2, 0)
        labelsGrid.add(status, 3, 0)
        labelsGrid.add(typeLabel, 0, 1)
        labelsGrid.add(type, 1, 1)
        labelsGrid.add(lastUpdatedLabel, 2, 1)
        labelsGrid.add(lastUpdated, 3, 1)

       // noteGrid.add(noteLabel, 0, 0)
        //noteGrid.add(TextFlow(note), 0, 0)

        val col1 = ColumnConstraints()
        col1.percentWidth = 14.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 34.0
        val col3 = ColumnConstraints()
        col3.percentWidth = 20.0
        val col4 = ColumnConstraints()
        col4.percentWidth = 32.0
        labelsGrid.columnConstraints.addAll(col1, col2, col3, col4)

        val col11 = ColumnConstraints()
        col11.percentWidth = 100.0
/*        val col12 = ColumnConstraints()
        col12.percentWidth = 86.0*/
        noteGrid.columnConstraints.addAll(col11) //col12)

        val headerGrid = GridPane()
        headerGrid.add(labelsGrid, 0, 0)
        headerGrid.add(noteGrid, 0, 1)

        val headerCol = ColumnConstraints()
        headerCol.percentWidth = 100.0
        headerGrid.columnConstraints.add(headerCol)

        //headerGrid.isGridLinesVisible = true

        return headerGrid
    }

    private fun selectAssignedLocalGroup() {
        //GridPane.setValignment(assignedLocal, VPos.CENTER)
        //GridPane.setHalignment(assignedLocal, HPos.CENTER)
        ticketActionGrid.add(assignedLocal, 1, 0)
    }

    private fun selectAssignedOutsideGroup() {
        //GridPane.setValignment(assignedOutside, VPos.CENTER)
        //GridPane.setHalignment(assignedOutside, HPos.CENTER)
        ticketActionGrid.add(assignedOutside, 1, 0)
    }

    private fun selectInProgressLocalGroup() {
        //GridPane.setValignment(inProgressLocal, VPos.CENTER)
        //GridPane.setHalignment(inProgressLocal, HPos.CENTER)
        ticketActionGrid.add(inProgressLocal, 1, 0)
    }

    private fun selectInProgressOutsideGroup() {
        //GridPane.setValignment(inProgressOutside, VPos.CENTER)
        //GridPane.setHalignment(inProgressOutside, HPos.CENTER)
        ticketActionGrid.add(inProgressOutside, 1, 0)
    }

    private fun configureTicketActionGrid() {

        val note = Text("")
        val viewNote = TextFlow(note)
        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            note.text = p0.value.lastNote
        }
        GridPane.setValignment(viewNote, VPos.CENTER)
        GridPane.setHalignment(viewNote, HPos.CENTER)
        ticketActionGrid.add(viewNote, 0, 0)

        val col1 = ColumnConstraints()
        col1.percentWidth = 48.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 52.0
        ticketActionGrid.columnConstraints.addAll(col1, col2)

        val row1 = RowConstraints()
        row1.percentHeight = 100.0
        ticketActionGrid.rowConstraints.addAll(row1)

    }

    private fun configureBorderPane() {
        /*val scanTicketsButton = Button("Scan in progress tickets & create excel status sheet")

        scanTicketsButton.onAction = javafx.event.EventHandler {

            val tickets = ITSMFunctions.fetchAllInProgressTickets()
            tickets.forEach { println(RecoupNotesParser.tscNumber(it.notes) + ",") }

            try {
                val template = FileInputStream(Config.readPathToTicketAnalysisTemplate())

                // Instantiate a Workbook object that represents an Excel file
                val workbook = XSSFWorkbook(template)
                val sheet = workbook.getSheetAt(0)

                val format = workbook.createDataFormat()
                val tscStyle = workbook.createCellStyle()
                tscStyle.dataFormat = format.getFormat("0")
                val dateStyle = workbook.createCellStyle()
                dateStyle.dataFormat = format.getFormat("d/m/yyyy h:mm")

                val startOfTableRowIndex = 4
                //val tscColumn = 1
                tickets.forEach {
                    val inputRow = sheet.createRow(startOfTableRowIndex+(tickets.indexOf(it)))
                    val incCell = inputRow.createCell(0)
                    incCell.setCellValue(it.incNumber)
                    val tscCell = inputRow.createCell(1)
                    tscCell.setCellValue(RecoupNotesParser.tscNumber(it.notes).toDouble())
                    tscCell.cellStyle = tscStyle
                    val lastModifiedCell = inputRow.createCell(3)
                    val dateFormatter = SimpleDateFormat("M/dd/yyyy hh:mm:ss aa")
                    lastModifiedCell.setCellValue(dateFormatter.parse(it.lastModified))
                    lastModifiedCell.cellStyle = dateStyle
                }

                template.close()
                val outFile = FileOutputStream(File(Config.readPathToTicketAnalysisSheet()))
                workbook.write(outFile)
                outFile.close()
                println("Excel file created and closed.")

            } catch (t: TicketIsNullException) {
                println("Must first fetch ticket.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //loop through all the in progress tickets until there are no more
            //get information and store in lists
            //input all information from lists into excel document
        }*/

        //Header Config
        val header = configureMainRegionHeader()
        BorderPane.setAlignment(header, Pos.TOP_CENTER)

        configureTicketActionGrid()
        BorderPane.setAlignment(ticketActionGrid, Pos.CENTER)
        //Right (Action buttons) Config

        //BorderPane.setAlignment(scanTicketsButton, Pos.CENTER)

        //val bPane = BorderPane()
        bPane.top = header
        bPane.center = ticketActionGrid
        //bPane.center = scanTicketsButton
        //return bPane
    }

    private fun configureSplitRoot() {
        AnchorPane.setBottomAnchor(splitRoot, 0.0)
        AnchorPane.setLeftAnchor(splitRoot, 0.0)
        AnchorPane.setRightAnchor(splitRoot, 0.0)
        AnchorPane.setTopAnchor(splitRoot, 0.0)

        splitRoot.items.addAll(configureHeader(), bPane)
        splitRoot.orientation = Orientation.VERTICAL

    }

    init {
        ticketModel.setCurrentTicket(Ticket("", "", ticketIndex = 2))

        configureBorderPane()
        configureSplitRoot()
        splitRoot.setDividerPosition(0, 0.10)

        ScreenController.addScene("scanInstructions", instructionScreen)
        instructionsBtn.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("scanInstructions")
        }

        //Make it so that when tickets are changed the button groups are changed similar to manual interface
        ticketModel.currentTicket.addListener { p0, oldValue, newValue ->
            when (oldValue.automaticButtonGroup) {
                ButtonGroups.ASSIGNEDLOCAL -> { ticketActionGrid.children.remove(assignedLocal) }
                ButtonGroups.ASSIGNEDOUTSIDE -> { ticketActionGrid.children.remove(assignedOutside) }
                ButtonGroups.INPROGRESSLOCAL -> { ticketActionGrid.children.remove(inProgressLocal) }
                ButtonGroups.INPROGRESSOUTSIDE -> { ticketActionGrid.children.remove(inProgressOutside) }
            }
            when (newValue.automaticButtonGroup) {
                ButtonGroups.ASSIGNEDLOCAL -> { selectAssignedLocalGroup() }
                ButtonGroups.ASSIGNEDOUTSIDE -> { selectAssignedOutsideGroup() }
                ButtonGroups.INPROGRESSLOCAL -> { selectInProgressLocalGroup() }
                ButtonGroups.INPROGRESSOUTSIDE -> { selectInProgressOutsideGroup() }
            }
        }

        this.children.add(splitRoot)
    }

}