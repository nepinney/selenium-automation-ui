package acquire.recoup.manual

import acquire.recoup.components.EmailComponent
import acquire.recoup.components.TicketModel
import acquire.recoup.components.TscComponent
import acquire.recoup.components.UpdateTicketComponent
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class OutsideRequest(private val ticketModel: TicketModel) : VBox() {

    private val tscInstructionLabel = Label("1. Open ticket on browser or enter tsc number to generate")
    private val tscComponent = TscComponent(ticketModel)

    private fun addTscComponent() {
        this.children.add(tscComponent)
    }

    private fun addEmailComponent() {
        this.children.add(EmailComponent("2. Create outside request email", ticketModel.currentTicket, ManualActionTypes.OUTSIDEREQUEST))
    }

    private fun addUpdateTicketComponent() {
        this.children.add(UpdateTicketComponent(ManualActionTypes.OUTSIDEREQUEST))
    }

    private fun configureLocalRequestComponent() {
        this.children.add(tscInstructionLabel)
        addTscComponent()
        addEmailComponent()
        addUpdateTicketComponent()
        this.alignment = Pos.TOP_LEFT
        this.spacing = 5.0
    }

    init {
        configureLocalRequestComponent()
    }
}