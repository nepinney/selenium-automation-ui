package acquire.recoup.components

import acquire.DriverFunctions
import acquire.PurolatorFunctions
import acquire.Ticket
import acquire.main
import acquire.recoup.MailingAddress
import acquire.recoup.RecoupNotesParser
import acquire.recoup.automatic.CreateWaybill
import javafx.beans.property.ObjectProperty
import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox

class AddressComponent(
        private val ticketProperty: ObjectProperty<Ticket>
) : GridPane() {

    private val attentionToGrid = GridPane()
    private val addressGrid = GridPane()

    val attentionToField = TextField("Attention To")
    val phoneNumberAreaField = TextField("Area")
    val phoneNumberField = TextField("Phone #")

    val streetNumberField = TextField("Street #")
    val streetNameField = TextField("Street Name")
    val postalCodeField = TextField("Postal Code")
    val cityField = TextField("City")
    val suiteNumberField = TextField("Suite #")
    val floorNumberField = TextField("Floor #")

    //private val createWaybillButton = Button("Create Waybill")

    private fun addListenersToTextFields() {
        ticketProperty.addListener { p0, p1, p2 ->
            if (RecoupNotesParser.clientToUpdateAndValidate(p0.value.notes).isBlank())
                println("Client to address not mentioned in ticket")
            else {
                attentionToField.text = RecoupNotesParser.attentionTo(p0.value.notes)
                streetNumberField.text = RecoupNotesParser.streetNumber(p0.value.notes)
                streetNameField.text = RecoupNotesParser.streetName(p0.value.notes)
                cityField.text = RecoupNotesParser.city(p0.value.notes)
                postalCodeField.text = RecoupNotesParser.postalCode(p0.value.notes)
            }
        }
    }

    private fun configureAttentionToGrid() {
        val col1 = ColumnConstraints()
        val col2 = ColumnConstraints()
        val col3 = ColumnConstraints()
        col1.percentWidth = 55.0
        col2.percentWidth = 10.0
        col3.percentWidth = 35.0

        attentionToGrid.add(attentionToField, 0, 0)
        attentionToGrid.add(phoneNumberAreaField, 1, 0)
        attentionToGrid.add(phoneNumberField, 2, 0)
        attentionToGrid.columnConstraints.addAll(col1, col2, col3)
        attentionToGrid.hgap = 5.0
        //GridPane.setHgrow(attentionToGridPane, Priority.NEVER)
    }

    private fun configureAddressGrid() {

        val col1 = ColumnConstraints()
        val col2 = ColumnConstraints()
        col1.percentWidth = 35.0
        col2.percentWidth = 65.0

        addressGrid.add(streetNumberField, 0, 0)
        addressGrid.add(streetNameField, 1, 0)
        addressGrid.add(postalCodeField, 0, 1)
        addressGrid.add(cityField, 1, 1)
        addressGrid.add(suiteNumberField, 0, 2)
        addressGrid.add(floorNumberField, 0, 3)
        /*generalAddressGridPane.add(createWaybillButton, 2, 3)
        GridPane.setHalignment(createWaybillButton, HPos.RIGHT)*/

        addressGrid.hgap = 5.0
        addressGrid.vgap = 5.0
        addressGrid.columnConstraints.addAll(col1, col2)
    }

    private fun configureThis() {
        val mainCol = ColumnConstraints()
        mainCol.percentWidth = 100.0
        this.columnConstraints.add(mainCol)
        this.vgap = 5.0
        //this.isGridLinesVisible = true

        this.add(attentionToGrid, 0, 0)
        this.add(addressGrid, 0, 1)
    }

    init {
        addListenersToTextFields()
        configureAddressGrid()
        configureAttentionToGrid()
        configureThis()
        //this.alignment = Pos.CENTER_RIGHT
/*        if (ticketProperty.value.clientToUpdateAndValidate.isBlank())
            println("Ticket does not contain an")*/

       // createWaybillButton.onAction = javafx.event.EventHandler {
            //CreateWaybill.createWaybill(MailingAddress("Bell", ...)) //TODO


            /*println("Creating Waybill")
            when (PurolatorFunctions.purolatorOpen) {
                true -> {
                    DriverFunctions.switchToTab("purolator")

                }
                false -> {
                    DriverFunctions.createNewTab("https://eshiponline.purolator.com/ShipOnline/SecurePages/Public/FormsLogin.aspx?ReturnUrl=/ShipOnline/Welcome.aspx&lang=E", "purolator")
                    PurolatorFunctions.login("jpaquete", "eusteam")
                    PurolatorFunctions.purolatorOpen = true
                    PurolatorFunctions.clickCreateShipmentButtonFromHomeScreen()
                    //PurolatorFunctions.enterShippingInformation("Nicholas Pinney","L6H 5T8", "2140", "Winding Woods Drive", "905", "2575603")
                    PurolatorFunctions.selectDropOff()
                }
            }*/

        //}

        //this.children.addAll(configureAttentionToGrid(), configureAddressFieldsGrid())
    }

}