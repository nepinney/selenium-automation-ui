package acquire.kotlin.recoup.action

import acquire.kotlin.Config
import acquire.kotlin.exception.TicketIsNullException
import acquire.kotlin.recoup.RecoupActionTypes
import acquire.kotlin.recoup.component.EmailComponent
import acquire.kotlin.recoup.component.TicketModel
import acquire.kotlin.recoup.component.TscComponent
import acquire.kotlin.recoup.component.UpdateTicketComponent
import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Confirmation(
        private val ticketModel: TicketModel
) : VBox() {

    private val tscInstructions = Label("1. Fetch notes of ticket to create confirmation excel file.")
    val excelInstructions = Label("2. Create excel confirmation file")
    private val tscComponent = TscComponent(ticketModel)

    private fun addTscComponent() {
        this.children.addAll(tscInstructions, tscComponent)
    }
    
    private fun addExcelFileCreationComponent(): GridPane {
        val excelFileCreationGrid = GridPane()

        val createConfirmationButton = Button("Create Excel Sheet")

        val materialCheckboxes = mapOf(
                Pair(CheckBox("Monitor"), ColumnConstraints()),
                Pair(CheckBox("Charger"), ColumnConstraints()),
                Pair(CheckBox("Mouse"), ColumnConstraints()),
                Pair(CheckBox("Keyboard"), ColumnConstraints()),
                Pair(CheckBox("Carrying Case"), ColumnConstraints())
        )

        var temp = mutableListOf(0, 1, 2, 3, 4)
        materialCheckboxes.forEach {
            if (temp.size > 2) it.value.percentWidth = 14.0 else it.value.percentWidth = 15.0
            if (temp.last() == temp.first()) it.value.percentWidth = 20.0
            excelFileCreationGrid.add(it.key, temp.first(), 0)
            temp.remove(temp.first())
            excelFileCreationGrid.columnConstraints.add(it.value)
        }

        excelFileCreationGrid.add(createConfirmationButton, 5, 0)
        val buttonConstraint = ColumnConstraints()
        buttonConstraint.percentWidth = 23.0
        excelFileCreationGrid.columnConstraints.add(buttonConstraint)

        GridPane.setHalignment(createConfirmationButton, HPos.RIGHT)
        GridPane.setHgrow(excelFileCreationGrid, Priority.ALWAYS)


        //TODO: Complete the excel section
        createConfirmationButton.onAction = javafx.event.EventHandler {
            try {
                if (ticketModel.currentTicket.value == null) throw TicketIsNullException()
                val template = FileInputStream(Config.readExcelConfirmationTemplateLocation())

                // Instantiate a Workbook object that represents an Excel file
                val workbook = HSSFWorkbook(template)
                val sheet = workbook.getSheetAt(0)

                val date = Date()
                val formatter = SimpleDateFormat("dd-MM-yy")
                val strDate = if (formatter.format(date).contains("."))
                    formatter.format(date).replace(".", "")
                else
                    formatter.format(date)

                //Indexes start at 0 for row and col
                val definiteCellsToEdit = mapOf(
                        Pair(sheet.getRow(1).getCell(1), strDate), //Date
                        Pair(sheet.getRow(1).getCell(3), ticketModel.currentTicket.value.tscNumber), //Tsc#
                        Pair(sheet.getRow(3).getCell(1), ticketModel.currentTicket.value.ownerName)) //User name
                definiteCellsToEdit.forEach { it.key.setCellValue(it.value) }

                val contentToInclude = mapOf(
                        Pair("Monitor", sheet.getRow(14).getCell(1)),
                        Pair("Charger", sheet.getRow(16).getCell(1)),
                        Pair("Keyboard", sheet.getRow(18).getCell(1)),
                        Pair("Mouse", sheet.getRow(19).getCell(1)),
                        Pair("Carrying Case", sheet.getRow(22).getCell(1))
                )
                //Mark the cells with an "X" that have been checked.
                materialCheckboxes!!.keys.filter { it.isSelected }.forEach {
                    contentToInclude[it.text]?.setCellValue("X")
                }

                val desktopCells = mapOf(
                        Pair(sheet.getRow(13).getCell(1), "X"),
                        Pair(sheet.getRow(13).getCell(3), ticketModel.currentTicket.value.deviceModel),
                        Pair(sheet.getRow(13).getCell(4), ticketModel.currentTicket.value.serialNumber),
                        Pair(sheet.getRow(15).getCell(3), ""),
                        Pair(sheet.getRow(15).getCell(4), "")
                )
                val laptopCells = mapOf(
                        Pair(sheet.getRow(15).getCell(1), "X"),
                        Pair(sheet.getRow(15).getCell(3), ticketModel.currentTicket.value.deviceModel),
                        Pair(sheet.getRow(15).getCell(4), ticketModel.currentTicket.value.serialNumber)
                )
                if (ticketModel.currentTicket.value.deviceType.equals("Desktop")) desktopCells.forEach { it.key.setCellValue(it.value) }
                else if (ticketModel.currentTicket.value.deviceType.equals("Laptop")) laptopCells.forEach { it.key.setCellValue(it.value) }

                template.close()
                val outFile = FileOutputStream(File("${Config.readSaveBuiltExcelConfirmationLocation()}PC return ${ticketModel.currentTicket.value.ownerName}.xls"))
                workbook.write(outFile)
                outFile.close()
                println("\nExcel file created successfully: ${Config.readSaveBuiltExcelConfirmationLocation()}PC return ${ticketModel.currentTicket.value.ownerName}.xls")

            } catch (t: TicketIsNullException) {
                println("Must first fetch ticket.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return excelFileCreationGrid
    }

    private fun addEmailConponent() {
        this.children.add(EmailComponent("3. Generate confirmation email", ticketModel.currentTicket, RecoupActionTypes.CONFIRMATION))
    }

    private fun addUpdateTicketComponent() {
        this.children.add(UpdateTicketComponent(RecoupActionTypes.CONFIRMATION))
    }

    init {
        this.spacing = 5.0
        addTscComponent()
        this.children.addAll(excelInstructions, addExcelFileCreationComponent())
        addEmailConponent()
        addUpdateTicketComponent()
    }


}