package acquire.kotlin.recoup.component

import acquire.kotlin.Ticket
import javafx.beans.property.ObjectProperty
import javafx.scene.control.Button
import java.lang.NullPointerException

class PrintTicketButton(
        private val ticket: ObjectProperty<Ticket>
) : Button("Print Ticket Details") {

    init {
        this.onAction = javafx.event.EventHandler {
            try {
                ticket.value.printInfo()
            } catch (e: NullPointerException) {
                println("Must first fetch ticket: $e")
            }
        }
    }

}