package acquire.kotlin.recoup.scan

import acquire.kotlin.*
import acquire.kotlin.exception.TicketIsNullException
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.math.absoluteValue

class ScanInterface : AnchorPane() {

    private fun configureInterface(): VBox {
        val root = VBox()
        AnchorPane.setBottomAnchor(root, 0.0)
        AnchorPane.setLeftAnchor(root, 0.0)
        AnchorPane.setRightAnchor(root, 0.0)
        AnchorPane.setTopAnchor(root, 0.0)
        root.spacing = 5.0
        root.alignment = Pos.CENTER

        val scanTicketsButton = Button("Scan in progress tickets & create excel status sheet")

        scanTicketsButton.onAction = javafx.event.EventHandler {

            val tickets = ITSMFunctions.fetchAllInProgressTickets()
            tickets.forEach { println(it.tscNumber + ",") }

            try {
                val template = FileInputStream("C:\\Users\\nick.pinney\\Documents\\inprogressticketanalysistemplate.xlsx")

                // Instantiate a Workbook object that represents an Excel file
                val workbook = XSSFWorkbook(template)
                val sheet = workbook.getSheetAt(0)

                /*println("$workbook")
                println("$sheet")*/

                var startOfTableRowIndex = 4
                val tscColumn = 1
/*                //val tickets = listOf(34, 55, 7, 6)
                val inputRow = sheet.createRow(4)
                val cell = inputRow.createCell(1)*/
                tickets.forEach {
                    val inputRow = sheet.createRow(startOfTableRowIndex+(tickets.indexOf(it)))
                    val inputCell = inputRow.createCell(tscColumn)
                    inputCell!!.setCellValue(it.tscNumber.toDouble())
                }

                template.close()
                val outFile = FileOutputStream(File("C:\\Users\\nick.pinney\\Documents\\GitHubProjects\\CGIAutomationV1\\resources\\inProgressTicketStatuses.xlsx"))
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

        root.children.addAll(scanTicketsButton, configureInstructionsButton(), BackButton("taskSelectionScene"))
        return root
    }


    private fun configureInstructionsButton(): Button {
        val inst = Button("View Instructions")

        val scanInstructions = GridPane()
        ScreenController.addScene("scanInstructions", scanInstructions)

        val label1 = Label("1. Scan Tickets Button")
        val label1Instructions = Text("The \"Scan Tickets\" button will scan all the tickets in your queue which are In Progress and create an excel sheet with their status so you may quickly view which ones need to be sent reminders.")
        scanInstructions.add(label1, 0, 0)
        scanInstructions.add(label1Instructions, 0, 1)
        scanInstructions.add(BackButton("recoupScan"), 0, 2)

        inst.onAction = javafx.event.EventHandler {
            ScreenController.activateScene("scanInstructions")
        }
        return inst
    }

    init {
        this.children.addAll(configureInterface())
    }

}