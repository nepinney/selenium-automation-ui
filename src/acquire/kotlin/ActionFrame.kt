package acquire.kotlin

import acquire.kotlin.recoup.RecoupActionTypes
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * @Author Nicholas Pinney
 */
class   ActionFrame : GridPane() {

    @FXML
    private lateinit var rootGrid: GridPane
    @FXML
    private lateinit var tscGrid: GridPane

    var actionType: RecoupActionTypes? = null

    //private var ticket: Ticket? = null //TODO: 1
    private var ticket = Ticket("Submit\n" +
            "ITEM DETAILS\n" +
            "Order number : 200528584702\n" +
            "Order date : 2020/05/28 10:01:43 AM\n" +
            "Requestor\n" +
            "Shuo Wang (6085588)\tEmail : SHUO.WANG@BELL.CA\n" +
            "Telephone\t(416) 353-4120\tCompany\tBELL MOBILITY (10)\n" +
            "Owner\n" +
            "Denisa Bani (EZ76005)\tEmail : DENISA.BANI@BELL.CA\n" +
            "Telephone\t\tCompany\tBELL MOBILITY(10)\n" +
            "Org. Code\tH8041051\tProvince\tOntario\n" +
            "Title\tINTERN, DATA SCIENCE\tBuilding code\tTOROONDC\n" +
            "Item summary\n" +
            "Denisa Bani (EZ76005)\n" +
            "Product\tAgent\tStatus\n" +
            "Return to Reuse Bell - Computer or Thin Client\tCGI DSM North York\tAssigned\n" +
            "Item details\n" +
            "Denisa Bani (EZ76005)\n" +
            "200528-5847-0201\tReturn to Reuse Bell - Computer or Thin Client\n" +
            "Status\tAssigned\n" +
            "Agent\tCGI DSM North York\n" +
            "Billing Reference Number\t001131769\n" +
            "Broad Service Category\tDisconnect\n" +
            "Contact Information\tDsm North York (DSNYRK)\n" +
            "Services - Entity - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Entity\tBell TV - Bell Mobility - Bell Mobility Channels - BDI\n" +
            "Configuration - Usage - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Usage\tPrimary : Sole and exclusive station for main job functions by an active employee.\n" +
            "Configuration - Return - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "A separate item is required per computer to manage inventories.\tLaptop\n" +
            "Configuration - Details - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Computer, peripherals, accessories and/or videozone devices to be returned\tLaptop\n" +
            "Carrying Case\n" +
            "AC Adapter\n" +
            "Please enter the quantities for each computers, peripherals, accessories and devices to be returned\t1,1,1\n" +
            "Please validate and update\tDenisa Bani\n" +
            "11 Orlando Blvd -\n" +
            "Toronto - ON M1R 3N5\n" +
            "Exact location: suite, room, section, column, cubicle, extension, etc.\tN/A\n" +
            "Configuration - Method - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "You may return the computer and accessories to a DEPOT or request a pickup.\tPlease arrange for a pick-up.\n" +
            "Your answer will determine which group will handle this request.\tON North York - 100 Wynford\n" +
            "Configuration - Contacts - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Primary (on-site)\tShuo Wang (6085588) - (416) 353-4120 - SHUO.WANG@BELL.CA\n" +
            "Secondary (on-site)\tJesung Park (6065773) - (416) 353-3987 - JESUNG.PARK@BELL.CA\n" +
            "Billing, Shipping & Installation - Desired Due Date - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Desired Due Date\t2020/05/29")

    @FXML private lateinit var tscField: TextField

    @FXML private lateinit var generateButton: Button
    @FXML private lateinit var emailButton: Button
    @FXML private lateinit var updateButton: Button
    @FXML private lateinit var saveButton: Button
    @FXML private lateinit var printTicketButton: Button

    @FXML private lateinit var label3: Label //Create request email label
    @FXML private lateinit var label4: Label //Update ticket label
    @FXML private lateinit var label5: Label //Save ticket label

    //Global variables for the createWaybill component
    private val waybillLabel = Label("2. Enter correct address after fetching notes")
    private var addressGrids: List<GridPane>? = null

    //Global variables for the confirmation component
    private var confirmationFunctionsGrid: GridPane? = null
    private var createConfirmationButton: Button? = null
    private var excelLabel: Label? = null
    private var materialCheckboxes: Map<CheckBox, ColumnConstraints>? = null

    private fun generateTicketFromBlankTscField(): Ticket {
        return Ticket(SeleniumMethods.getNotesFromTicket(true))
    }

    private fun generateTicketFromTSCField(): Ticket {
        SeleniumMethods.searchOrderNumber(tscField.text)
        SeleniumMethods.selectSearchResult()
        return Ticket(SeleniumMethods.getNotesFromTicket(false))
    }


    @FXML
    fun fetchTicket() {
        /*ticket = null //TODO: 2
        ticket = when (tscField.text.isBlank()) {
            true -> {
                generateTicketFromBlankTscField()
            }
            false -> {
                generateTicketFromTSCField()
            }
        }*/
        //println(ticket.tscNumber)
        tscField.text = ticket!!.tscNumber
        //ticket.printInfo()
        // println("Ticket Generated!")
    }

    private fun generateEmailContent(emailTemplatePath: String): List<String> {
        val emailVariables = mapOf(
            Pair("<tsc>", ticket!!.tscNumber),
            Pair("<owner>", ticket!!.ownerName),
            Pair("<model>", ticket!!.deviceModel),
            Pair("<sn>", ticket!!.serialNumber),
            Pair("<address>", ticket!!.clientToUpdateAndValidate),
            Pair("<deviceL>", ticket!!.deviceType.first().toLowerCase() + ticket!!.deviceType.slice(1..ticket!!.deviceType.lastIndex)),
            Pair("<device>", ticket!!.deviceType)
            //Pair("<device>", t!!.device)
        )

        val emailTemplate = File(emailTemplatePath)
        var fileScanner = Scanner(emailTemplate).useDelimiter("---End")

        var formattedEmail = fileScanner.next()
        for (variable in emailVariables) {
            formattedEmail = formattedEmail.replace(variable.key, variable.value.toString())
        }
        fileScanner.close()
        fileScanner = Scanner(formattedEmail).useDelimiter("---")
        //println("-------------------------------------------------------------------------")
        //println("1: ${fileScanner.next()}")
        //println("2: ${fileScanner.next().trim()}")
        val emailComponents = listOf(fileScanner.next(), fileScanner.next().trim())
        fileScanner.close()
        return emailComponents
    }

    private fun buildEmail(email: List<String>) {

        val validRecipients = ticket!!.validUniqueEmailAddresses()

        try {
            val message: Message = MimeMessage(Session.getInstance(System.getProperties()))

            message.subject = email.first()
            //message.setFrom(InternetAddress("nick.pinney@cgi.com"))
            message.setHeader("X-Unsent", "1")
            // create the message part
            val content = MimeBodyPart()
            // fill message
            content.setText(email.last())
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(content)

            when (actionType) {
                RecoupActionTypes.CONFIRMATION -> {
                    message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse(
                            if (validRecipients.contains(ticket!!.requestorEmail))
                                ticket!!.requestorEmail
                            else
                                validRecipients.first()
                        )
                    )

                    message.addRecipient(Message.RecipientType.CC, InternetAddress("john.sullivan@cgi.com"))
                    message.addRecipient(Message.RecipientType.CC, InternetAddress("sayan.sengupta@cgi.com"))
                    try {
                        // add attachments
                        val attachPart = MimeBodyPart()
                        val attachmentPath =
                            "${Config.readSaveBuiltExcelConfirmationLocation()}\\PC return ${ticket!!.ownerName}.xls"
                        attachPart.attachFile(attachmentPath)
                        multipart.addBodyPart(attachPart)
                    } catch (e: Exception) {
                        println("$e: No such file to attach to the email.")
                    }
                }
                else -> {
                    when (validRecipients.count()) {
                        1 -> {
                            message.setRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(validRecipients.first())
                            )
                        }
                        2 -> {
                            if (validRecipients.first().equals(ticket!!.primaryEmail)) {
                                message.setRecipients(
                                    Message.RecipientType.TO,
                                    InternetAddress.parse(validRecipients.first())
                                )
                                message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(validRecipients.last())
                                )
                            } else {
                                message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(validRecipients.first())
                                )
                                message.setRecipients(
                                    Message.RecipientType.TO,
                                    InternetAddress.parse(validRecipients.last())
                                )
                            }
                        }
                        3 -> {
                            message.setRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(ticket!!.primaryEmail)
                            )
                            message.setRecipients(
                                Message.RecipientType.CC,
                                InternetAddress.parse(ticket!!.requestorEmail)
                            )
                            message.setRecipients(
                                Message.RecipientType.CC,
                                InternetAddress.parse(ticket!!.secondaryEmail)
                            )
                        }
                    }
                }
            }
            // integration
            message.setContent(multipart)
            // store file
            val out = FileOutputStream(File(Config.readSaveEmailLocation()))
            message.writeTo(out)
            out.close()
            println("\nEmail saved successfully to: ${Config.readSaveEmailLocation()}")
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        //return Config.readSaveEmailLocation()
    }

    private fun launchOutlook() {
        try {
            val command = File(Config.readPathToOutlookExe()).absolutePath.replace('\\','/' ) + " /eml " + File(Config.readSaveEmailLocation().toString()).absolutePath.replace('\\','/')
            println("Executed command: $command")

            val builder = ProcessBuilder(File(Config.readPathToOutlookExe()).absolutePath.replace('\\','/' ), "/eml", File(Config.readSaveEmailLocation().toString()).absolutePath.replace('\\','/') )
            val process = builder.start()
            val result = process.waitFor()
            println("Process exited with result $result")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @FXML
    fun createEmail() {
        //TODO: if a file is open, need to generate another file name or close the current open file to open the new generated email if button clicked
        try {
            if (ticket == null)
                throw TicketIsNullException()
            when (actionType) {
                RecoupActionTypes.LOCALREQUEST -> {
                    //println("LOCALREQUEST EMAIL")
                    buildEmail(generateEmailContent(Config.readLocalRequestEmailLocation()))
                    launchOutlook()
                }
                RecoupActionTypes.OUTSIDEREQUEST -> {
                    //println("OUTSIDEREQUEST EMAIL")
                    buildEmail(generateEmailContent(Config.readOutsideRequestEmailLocation()))
                    launchOutlook()
                }
                RecoupActionTypes.CREATEWAYBILL -> {
                    buildEmail(generateEmailContent(Config.readConfirmShipmentEmailLocation()))
                    launchOutlook()
                }
                RecoupActionTypes.CONFIRMATION -> {
                    //println("CONFIRMATION EMAIL")
                    buildEmail(generateEmailContent(Config.readConfirmationRequestEmailLocation()))
                    launchOutlook()
                }
            }
        }
        catch (e: TicketIsNullException) {
            println(e)
        }
    }

    @FXML
    fun updateTicket() {
        when (actionType) {
            RecoupActionTypes.CONFIRMATION -> {
                SeleniumMethods.setStatusField("pending")
                SeleniumMethods.setStatusReasonField()
                SeleniumMethods.setResolution("Recoup completed.")
                SeleniumMethods.setNotes("Recoup completed.")
                SeleniumMethods.setTargetDate()
            }
            else -> {
                SeleniumMethods.setStatusField("in progress")
            }
        }
    }

    @FXML
    fun saveTicketAndClear() { }

    @FXML
    fun printTicket() {
        ticket!!.printInfo()
    }

    @FXML
    fun onTextFieldClick() {
        when (true) {
            tscField.text.isBlank() -> { }
            tscField.style == "-fx-faint-focus-color: transparent ; -fx-focus-color: green ;" -> {
                tscField.text = ""
                setTextFieldStyleDefault(tscField)
            }
            true -> {
                val clipboard = Clipboard.getSystemClipboard()
                val content = ClipboardContent()
                content.putString(tscField.text)
                clipboard.setContent(content)
                setTextFieldStyleGreen(tscField)
            }
        }
    }

    fun alterLayoutForCreateWaybill() {

        rootGrid.children.removeAll(label3, label4, label5, updateButton, saveButton, emailButton)

        when (null) {
            addressGrids -> {
                //Grid for attention to field & phone #
                val attentionToGridPane = GridPane()
                val attentionToField = Pair(TextField("Attention To"), ColumnConstraints())
                val phoneNumberField = Pair(TextField("Phone #"), ColumnConstraints())
                attentionToField.second.percentWidth = 35.0
                phoneNumberField.second.percentWidth = 25.0
                val grid1Col3 = ColumnConstraints()
                grid1Col3.percentWidth = 40.0
                attentionToGridPane.add(attentionToField.first, 0, 0)
                attentionToGridPane.add(phoneNumberField.first, 1, 0)
                attentionToGridPane.columnConstraints.addAll(attentionToField.second, phoneNumberField.second, grid1Col3)
                attentionToGridPane.hgap = 5.0
                GridPane.setHgrow(attentionToGridPane, Priority.NEVER)
                
                //Grid for street#, name, postal code, city, suite and floor #
                val generalAddressGridPane = GridPane()
                val streetNumberField = Pair(TextField("Street #"), ColumnConstraints())
                val streetNameField = Pair(TextField("Street Name"), ColumnConstraints())
                val postalCodeField = TextField("Postal Code")
                val cityField = TextField("City")
                val suiteNumberField = TextField("Suite #")
                val floorNumberField = TextField("Floor #")
                val createWaybillButton = Button("Create Waybill")
                GridPane.setHalignment(createWaybillButton, HPos.RIGHT)
                val col3 = ColumnConstraints()
                streetNumberField.second.percentWidth = 25.0
                streetNameField.second.percentWidth = 35.0
                col3.percentWidth = 40.0
                generalAddressGridPane.add(streetNumberField.first, 0, 0)
                generalAddressGridPane.add(streetNameField.first, 1, 0)
                generalAddressGridPane.add(postalCodeField, 0, 1)
                generalAddressGridPane.add(cityField, 1, 1)
                generalAddressGridPane.add(suiteNumberField, 0, 2)
                generalAddressGridPane.add(floorNumberField, 0, 3)
                generalAddressGridPane.add(createWaybillButton, 2, 3)
                generalAddressGridPane.columnConstraints.addAll(streetNumberField.second, streetNameField.second, col3)
                generalAddressGridPane.hgap = 5.0
                generalAddressGridPane.vgap = 6.0
                GridPane.setHgrow(createWaybillButton, Priority.NEVER)

                createWaybillButton.onAction = javafx.event.EventHandler{
                    println("Waybill Created!!!:)")
                }

                addressGrids = listOf(attentionToGridPane, generalAddressGridPane)
            }
        }

        label3.text = "3. Create shipping information email"

        rootGrid.add(waybillLabel, 0, 3) //column, row
        rootGrid.add(addressGrids!!.first(), 0, 4) //column, row
        rootGrid.add(addressGrids!!.last(), 0, 5) //column, row
        rootGrid.add(label3, 0, 6) //column, row
        rootGrid.add(emailButton, 0, 7) //column, row
    }

    fun removeCreateWaybillChanges() {
        rootGrid.children.removeAll(waybillLabel, emailButton, label3)
        addressGrids?.forEach { rootGrid.children.remove(it) }
        label3.text = "2. Create request email"
        rootGrid.add(label3, 0, 4)
        rootGrid.add(emailButton, 0, 5)
        rootGrid.children.addAll(label4, label5, updateButton, saveButton)
    }

    fun alterLayoutForConfirmation() {
        excelLabel = Label("2. Create excel confirmation sheet")
        createConfirmationButton = Button("Create Excel Sheet")

        materialCheckboxes = mapOf(
            Pair(CheckBox("Monitor"), ColumnConstraints()),
            Pair(CheckBox("Charger"), ColumnConstraints()),
            Pair(CheckBox("Mouse"), ColumnConstraints()),
            Pair(CheckBox("Keyboard"), ColumnConstraints()),
            Pair(CheckBox("Carrying Case"), ColumnConstraints())
        )

        confirmationFunctionsGrid = GridPane()

        var temp = mutableListOf(0, 1, 2, 3, 4)
        materialCheckboxes!!.forEach {
            if (temp.size > 2) it.value.percentWidth = 14.0 else it.value.percentWidth = 15.0
            if (temp.last() == temp.first()) it.value.percentWidth = 20.0
            confirmationFunctionsGrid!!.add(it.key, temp.first(), 0)
            temp.remove(temp.first())
            confirmationFunctionsGrid!!.columnConstraints.add(it.value)
        }

        confirmationFunctionsGrid!!.add(createConfirmationButton, 5, 0)
        val buttonConstraint = ColumnConstraints()
        buttonConstraint.percentWidth = 23.0
        confirmationFunctionsGrid!!.columnConstraints.add(buttonConstraint)

        GridPane.setHalignment(createConfirmationButton, HPos.RIGHT)
        GridPane.setHgrow(confirmationFunctionsGrid, Priority.ALWAYS)


        //TODO: Complete the excel section
        createConfirmationButton!!.onAction = javafx.event.EventHandler{
            try {
                if (ticket == null) throw TicketIsNullException()
                val template = FileInputStream(Config.readExcelConfirmationTemplateLocation())

                // Instantiate a Workbook object that represents an Excel file
                val workbook = HSSFWorkbook(template)
                val sheet = workbook.getSheetAt(0)

                val date = Date()
                val formatter = SimpleDateFormat("dd-MMM-yy")
                val strDate = formatter.format(date)

                //Indexes start at 0 for row and col
                val definiteCellsToEdit = mapOf(
                    Pair(sheet.getRow(1).getCell(1), strDate), //Date
                    Pair(sheet.getRow(1).getCell(3), ticket!!.tscNumber), //Tsc#
                    Pair(sheet.getRow(3).getCell(1), ticket!!.ownerName)) //User name
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
                    Pair(sheet.getRow(13).getCell(3), ticket!!.deviceModel),
                    Pair(sheet.getRow(13).getCell(4), ticket!!.serialNumber),
                    Pair(sheet.getRow(15).getCell(3), ""),
                    Pair(sheet.getRow(15).getCell(4), "")
                )
                val laptopCells = mapOf(
                    Pair(sheet.getRow(15).getCell(1), "X"),
                    Pair(sheet.getRow(15).getCell(3), ticket!!.deviceModel),
                    Pair(sheet.getRow(15).getCell(4), ticket!!.serialNumber)
                )
                if (ticket!!.deviceType.equals("Desktop")) desktopCells.forEach { it.key.setCellValue(it.value) }
                else if (ticket!!.deviceType.equals("Laptop")) laptopCells.forEach { it.key.setCellValue(it.value) }

                template.close()
                val outFile = FileOutputStream(File("${Config.readSaveBuiltExcelConfirmationLocation()}PC return ${ticket!!.ownerName}.xls"))
                workbook.write(outFile)
                outFile.close()
                println("\nExcel file created successfully: ${Config.readSaveBuiltExcelConfirmationLocation()}PC return ${ticket!!.ownerName}.xls")

            } catch (t: TicketIsNullException) {
                t.printStackTrace()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val labels = mapOf(
            Pair(label3, "3. Create request email"),
            Pair(label4, "4. Update the ticket status and add email to notes"),
            Pair(label5, "5. Save ticket and clear"))
        labels.forEach { it.key.text = it.value }

        rootGrid.add(confirmationFunctionsGrid, 0, 3)
        rootGrid.add(excelLabel, 0, 2)
    }

    fun removeConfirmationChanges() {
        val labels = mapOf(
            Pair(label3, "2. Create request email"),
            Pair(label4, "3. Update the ticket status and add email to notes"),
            Pair(label5, "4. Save ticket and clear"))
        labels.forEach { it.key.text = it.value }

        rootGrid.children.removeAll(excelLabel!!, confirmationFunctionsGrid!!)
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/action_frame.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
        fixGridPositions(tscGrid)
    }

    companion object {

        fun setTextFieldStyleGreen(tscField: TextField) { tscField.style = "-fx-faint-focus-color: transparent ; -fx-focus-color: green ;" }

        fun setTextFieldStyleDefault(tscField: TextField) { tscField.style = "" }

        fun fixGridPositions(childGrid: GridPane) {
            val col1 = ColumnConstraints()
            col1.percentWidth = 15.0
            val col2 = ColumnConstraints()
            col2.percentWidth = 40.0
            val col3 = ColumnConstraints()
            col3.percentWidth = 45.0
            GridPane.setHgrow(childGrid.children[1], Priority.NEVER)
            childGrid.columnConstraints.addAll(col1,col2,col3)
        }

    }

}