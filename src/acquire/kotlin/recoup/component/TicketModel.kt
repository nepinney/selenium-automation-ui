package acquire.kotlin.recoup.component

import acquire.kotlin.Ticket
import acquire.kotlin.recoup.RecoupActionTypes
import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import java.util.concurrent.Callable

class TicketModel {
    val currentTicket: ObjectProperty<Ticket> = SimpleObjectProperty(null, "currentTicket")

    fun setCurrentTicket(currentTicket: Ticket) {
        this.currentTicket.set(currentTicket)
    }

}