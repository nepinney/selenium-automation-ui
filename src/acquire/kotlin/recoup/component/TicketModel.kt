package acquire.kotlin.recoup.component

import acquire.kotlin.Ticket
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

class TicketModel {
    val currentTicket: ObjectProperty<Ticket> = SimpleObjectProperty(null, "currentTicket")

    fun setCurrentTicket(currentTicket: Ticket) {
        this.currentTicket.set(currentTicket)
    }

}