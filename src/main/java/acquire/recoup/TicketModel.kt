package acquire.recoup

import acquire.Ticket
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

object TicketModel {
    val currentTicket: ObjectProperty<Ticket> = SimpleObjectProperty(null, "currentTicket")

    fun setCurrentTicket(currentTicket: Ticket) {
        this.currentTicket.set(currentTicket)
    }

}