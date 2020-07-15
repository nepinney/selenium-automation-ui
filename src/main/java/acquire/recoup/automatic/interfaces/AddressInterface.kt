package acquire.recoup.automatic.interfaces

import acquire.BackButton
import acquire.recoup.components.AddressComponent
import acquire.recoup.components.TicketModel
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox

class AddressInterface(private val ticketModel: TicketModel) : BorderPane() {

    private val buttonsBox = HBox()

    private val backBtn = BackButton("recoupScan")
    private val createWaybillBtn = Button("Create Waybill")

    private val addressComponent = AddressComponent(ticketModel.currentTicket)

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(createWaybillBtn, backBtn)
    }

    init {

        configureButtonsBox()

        this.center = addressComponent
        BorderPane.setMargin(addressComponent, Insets(5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 5.0, 5.0, 5.0))
    }

}