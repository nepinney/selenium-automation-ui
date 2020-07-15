package acquire.recoup.components

import acquire.DriverFunctions
import acquire.PurolatorFunctions
import acquire.Ticket
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
) : VBox() {

    private val attentionToField = TextField("Attention To")
    private val phoneNumberField = TextField("Phone #")

    private val streetNumberField = TextField("Street #")
    private val streetNameField = TextField("Street Name")
    private val postalCodeField = TextField("Postal Code")
    private val cityField = TextField("City")
    private val suiteNumberField = TextField("Suite #")
    private val floorNumberField = TextField("Floor #")

    private val createWaybillButton = Button("Create Waybill")

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

    private fun configureAttentionToGrid(): GridPane {
        val attentionToGridPane = GridPane()

        val col1 = ColumnConstraints()
        val col2 = ColumnConstraints()
        val col3 = ColumnConstraints()
        col1.percentWidth = 35.0
        col2.percentWidth = 25.0
        col3.percentWidth = 40.0
        attentionToGridPane.add(attentionToField, 0, 0)
        attentionToGridPane.add(phoneNumberField, 1, 0)
        attentionToGridPane.columnConstraints.addAll(col1, col2, col3)
        attentionToGridPane.hgap = 5.0
        //GridPane.setHgrow(attentionToGridPane, Priority.NEVER)
        return attentionToGridPane
    }

    private fun configureAddressFieldsGrid(): GridPane {
        val generalAddressGridPane = GridPane()

        val col1 = ColumnConstraints()
        val col2 = ColumnConstraints()
        val col3 = ColumnConstraints()
        col1.percentWidth = 25.0
        col2.percentWidth = 35.0
        col3.percentWidth = 40.0

        generalAddressGridPane.add(streetNumberField, 0, 0)
        generalAddressGridPane.add(streetNameField, 1, 0)
        generalAddressGridPane.add(postalCodeField, 0, 1)
        generalAddressGridPane.add(cityField, 1, 1)
        generalAddressGridPane.add(suiteNumberField, 0, 2)
        generalAddressGridPane.add(floorNumberField, 0, 3)
        generalAddressGridPane.add(createWaybillButton, 2, 3)
        GridPane.setHalignment(createWaybillButton, HPos.RIGHT)

        generalAddressGridPane.hgap = 5.0
        generalAddressGridPane.vgap = 6.0
        generalAddressGridPane.columnConstraints.addAll(col1, col2, col3)
        return generalAddressGridPane
    }

    init {
        this.spacing = 5.0
        //this.alignment = Pos.CENTER_RIGHT
/*        if (ticketProperty.value.clientToUpdateAndValidate.isBlank())
            println("Ticket does not contain an")*/
        addListenersToTextFields()

        createWaybillButton.onAction = javafx.event.EventHandler {
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

        }

        this.children.addAll(configureAttentionToGrid(), configureAddressFieldsGrid())
    }

}