package acquire.recoup.automatic.interfaces

import acquire.*
import acquire.recoup.RecoupNotesParser
import acquire.recoup.WaybillInformation
import acquire.recoup.components.AddressComponent
import acquire.recoup.TicketModel
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.*
import org.openqa.selenium.NoSuchElementException
import java.util.*

class AddressInterface : BorderPane(), ListenerHandle {

    private val rootPane = BorderPane()
    private val headerGrid = GridPane()
    private val buttonsBox = HBox()
    private val noteArea = TextArea()

    private val backBtn = BackButton("recoupScan")
    private val createWaybillBtn = Button("Create Waybill")

    private val addressComponent = AddressComponent()

    //Listeners
    private val listenerToUpdateNoteArea = ChangeListener<Ticket>() { observableValue: ObservableValue<out Ticket>, oldTicket: Ticket?, newTicket: Ticket? ->
        noteArea.text = "***FOR REFERENCE***\n" +
                RecoupNotesParser.clientToUpdateAndValidate(observableValue.value.notes) +
                "\n\n" +
                RecoupNotesParser.quantityInstructions(observableValue.value.notes) +
                "\n\n" +
                RecoupNotesParser.exactLocation(observableValue.value.notes) +
                observableValue.value.lastNote
    }

    override fun activateListeners() {
       // println("Activate listeners was called in AddressInterface")
        addressComponent.activateListeners()
        TicketModel.currentTicket.addListener(listenerToUpdateNoteArea)
    }

    override fun deactivateListeners() {
        addressComponent.deactivateListeners()
        TicketModel.currentTicket.removeListener(listenerToUpdateNoteArea)
    }

    private fun configureRootGrid() {

        rootPane.top = addressComponent
        rootPane.center = noteArea
        BorderPane.setMargin(noteArea, Insets(5.0, 0.0, 0.0, 0.0))

        /*rootGrid.hgap = 5.0
        val col1 = ColumnConstraints()
        col1.percentWidth = 100.0
        rootGrid.columnConstraints.addAll(col1)

        val row1 = RowConstraints()
        row1.percentHeight = 50.0
        val row2 = RowConstraints()
        row2.percentHeight = 50.0
        rootGrid.rowConstraints.addAll(row1, row2)

        rootGrid.add(addressComponent, 0, 0)
        rootGrid.add(noteArea, 0, 1)*/

        //For vertical layout
        /*val col1 = ColumnConstraints()
        col1.percentWidth = 40.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 60.0
        rootGrid.columnConstraints.addAll(col1, col2)

        val row1 = RowConstraints()
        row1.percentHeight = 100.0
        rootGrid.rowConstraints.add(row1)

        rootGrid.add(noteArea, 0, 0)
        rootGrid.add(addressComponent, 1, 0)*/
        //rootGrid.isGridLinesVisible = true

    }

    private fun configureLabelHeader() {
        headerGrid.add(Label("Client address information:"), 0, 0)
    }

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(createWaybillBtn, backBtn)
    }

    init {
        configureLabelHeader()
        configureRootGrid()
        configureButtonsBox()

        this.top = headerGrid
        BorderPane.setMargin(headerGrid, Insets(5.0, 5.0, 0.0, 5.0))
        //this.top.maxWidth(10.0)
        this.center = rootPane
        BorderPane.setMargin(rootPane, Insets(5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 5.0, 5.0, 5.0))

        createWaybillBtn.onAction = javafx.event.EventHandler {

            //TODO("Check the fields to make sure they are filled")
            when (addressComponent.checkFields()) {
                true -> {
                    //TODO("Create a new Waybill Information object")
                    val address = WaybillInformation(
                            "Bell",
                            addressComponent.attentionToField.text,
                            addressComponent.streetNumberField.text,
                            addressComponent.streetNameField.text,
                            addressComponent.postalCodeField.text,
                            addressComponent.cityField.text,
                            addressComponent.phoneNumberAreaField.text,
                            addressComponent.phoneNumberField.text,
                            if (addressComponent.floorNumberField.text.isBlank()) null else addressComponent.floorNumberField.text,
                            if (addressComponent.suiteNumberField.text.isBlank()) null else addressComponent.suiteNumberField.text)

                    try {
                        //TODO("Open a new Tab if purolator isn't yet open and logged in")
                        PurolatorFunctions.purolatorOpen = DriverFunctions.driver!!.windowHandles.count() > 1
                        println("Checked window handles, purolator open?: ${PurolatorFunctions.purolatorOpen}")
                        when (PurolatorFunctions.purolatorOpen) {
                            true -> {
                                PurolatorFunctions.switchToPurolatorTab()
                            }
                            false -> {
                                PurolatorFunctions.createPurolatorTab()
                                //PurolatorFunctions.purolatorOpen = true
                            }
                        }


                        //TODO("Populate fields on purolator website")
                        PurolatorFunctions.createNewShipment()
                        PurolatorFunctions.fillAddressFields(address)

                        //TODO("Ask for pins")
                        val scan = Scanner(System.`in`)
                        print("Enter outbound waybill pin: ")
                        val outbound = scan.nextLine().trim()
                        print("Enter inbound waybill pin: ")
                        val inbound = scan.nextLine().trim()
                        address.outboundPin = outbound
                        address.inboundPin = inbound

                        var choice: String
                        do {
                            print("Add work info containing shipment information to ticket? (y/n): ")
                            choice = scan.next()
                            scan.nextLine()
                        } while (choice != "y" && choice != "n" && choice != "Y" && choice != "N")

                        when (choice == "y" || choice == "Y") {
                            true -> {
                                DriverFunctions.switchToTab("itsm")
                                val str = "Return package shipped to ${address.streetNumber + " " + address.streetName} addressed to ${address.atnTo}." +
                                        "\nOutbound pin: $outbound" +
                                        "\nInbound pin: $inbound"
                                ITSMFunctions.addNotesToWorkInfoTextArea(str)
                                ITSMFunctions.saveNewWorkInfo()
                            }
                            false -> {
                                println("Sucks")
                            }
                        }
                        scan.close()

                        //TODO("Create email with pins and add that to ticket notes")
                        val shipmentEmail = Email()
                        shipmentEmail.buildShipmentConfirmationEmail(address)
                        shipmentEmail.buildEmlFile()
                        shipmentEmail.launchOutlook()
                    }
/*                    catch(n: NoSuchElementException) {

                        println("Someone already logged into Purolator:(")
                    }*/
                    catch (e: Exception) {
                        DriverFunctions.closeTab("purolator")
                        //println("Caught error during waybill creation")
                        //e.printStackTrace()
                    }

                }
                false -> {
                    println("Please fill all red boxes.")
                }
            }
            //PurolatorFunctions.enterShippingInformation("Nicholas Pinney","L6H 5T8", "2140", "Winding Woods Drive", "905", "2575603")
        }

    }

}