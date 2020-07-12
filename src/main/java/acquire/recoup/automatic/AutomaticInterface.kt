package acquire.recoup.automatic

import acquire.BackButton
import acquire.Config
import acquire.ITSMFunctions
import acquire.ScreenController
import acquire.exception.TicketIsNullException
import acquire.recoup.RecoupNotesParser
import acquire.recoup.components.TicketModel
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.layout.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class AutomaticInterface : AnchorPane() {

    private val splitRoot = SplitPane()

    private val ticketModel = TicketModel()

    private val startBtn = StartButton(ticketModel)
    private val endBtn = Button("End")
    private val instructionsBtn = Button("Instructions")
    private val instructionScreen = AutomaticInstructions()

    private fun configureHeader(): HBox {
        val box = HBox()
        box.spacing = 5.0
        box.children.addAll(startBtn, endBtn, BackButton("taskSelectionScene"))
        box.alignment = Pos.CENTER
        return box
    }

    private fun configureMainRegionHeader(): GridPane {
        val grid = GridPane()
        val tscLabel = Label("TSC #: ")
        val tsc = Label("")
        val statusLabel = Label("Status: ")
        val status = Label("")
        val lastUpdatedLabel = Label("Ticket last updated: ")
        val lastUpdated = Label("")
        val typeLabel = Label("Type: ")
        val type = Label("")

        val textItems = listOf(tscLabel, tsc, statusLabel, status, lastUpdatedLabel, lastUpdated, typeLabel, type)
        textItems.forEach{ it.style = "-fx-font-size: 150%;" }
        textItems.filter { textItems.indexOf(it) % 2 == 1 }
                 .forEach { it.style = "-fx-font-weight: bold; -fx-font-size: 150%;" }

        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            tsc.text = RecoupNotesParser.tscNumber(p0.value.notes)
        }

        grid.add(tscLabel, 0, 0)
        grid.add(tsc, 1, 0)
        grid.add(statusLabel, 2, 0)
        grid.add(status, 3, 0)
        grid.add(typeLabel, 0, 1)
        grid.add(type, 1, 1)
        grid.add(lastUpdatedLabel, 2, 1)
        grid.add(lastUpdated, 3, 1)

        val col1 = ColumnConstraints()
        col1.percentWidth = 12.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 28.0
        val col3 = ColumnConstraints()
        col3.percentWidth = 25.0
        val col4 = ColumnConstraints()
        col3.percentWidth = 35.0
        grid.columnConstraints.addAll(col1,col2,col3, col4)

        return grid
    }

    private fun configureMainRegion(): BorderPane {
        val scanTicketsButton = Button("Scan in progress tickets & create excel status sheet")

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

                var startOfTableRowIndex = 4
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
        }

        val header = configureMainRegionHeader()
        BorderPane.setAlignment(header, Pos.TOP_CENTER)
        BorderPane.setAlignment(scanTicketsButton, Pos.CENTER)

        val bPane = BorderPane()
        bPane.top = header
        bPane.center = scanTicketsButton
        return bPane
    }

    private fun configureSplitRoot() {
        AnchorPane.setBottomAnchor(splitRoot, 0.0)
        AnchorPane.setLeftAnchor(splitRoot, 0.0)
        AnchorPane.setRightAnchor(splitRoot, 0.0)
        AnchorPane.setTopAnchor(splitRoot, 0.0)

        splitRoot.items.addAll(configureHeader(), configureMainRegion())
        splitRoot.orientation = Orientation.VERTICAL

    }

    init {
        configureSplitRoot()
        splitRoot.setDividerPosition(0, 90.0)

        ScreenController.addScene("scanInstructions", instructionScreen)
        instructionsBtn.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("scanInstructions")
        }

        this.children.add(splitRoot)
    }

}