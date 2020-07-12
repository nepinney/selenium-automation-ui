package acquire.recoup.manual

import acquire.recoup.components.AddressComponent
import acquire.recoup.components.EmailComponent
import acquire.recoup.components.TicketModel
import acquire.recoup.components.TscComponent
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class CreateWaybill(private val ticketModel: TicketModel) : VBox() {

    private val tscInstructionLabel = Label("1. Open ticket on browser or enter tsc number to generate address")
    private val tscComponent = TscComponent(ticketModel)

    private fun addTscComponent() {
        this.children.add(tscComponent)
    }

    private fun addAddressComponent() {
        this.children.add(AddressComponent("2. Create waybill to recipient below", ticketModel.currentTicket))
    }

    private fun addEmailComponent() {
        this.children.add(EmailComponent("3. Create email with Purolator information", ticketModel.currentTicket, ManualActionTypes.CREATEWAYBILL))
    }

    init {
        this.spacing = 5.0
        this.children.add(tscInstructionLabel)
        addTscComponent()
        addAddressComponent()
        addEmailComponent()


    }

}