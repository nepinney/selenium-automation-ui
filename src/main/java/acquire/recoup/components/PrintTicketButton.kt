package acquire.recoup.components

import acquire.Ticket
import acquire.recoup.RecoupNotesParser
import javafx.beans.property.ObjectProperty
import javafx.scene.control.Button
import java.lang.NullPointerException

class PrintTicketButton(
        private val ticket: ObjectProperty<Ticket>
) : Button("Print Ticket Details") {

    init {
        this.onAction = javafx.event.EventHandler {
            try {
                RecoupNotesParser.printInfo(ticket.value.notes)
            } catch (e: NullPointerException) {
                println("Must first fetch ticket: $e")
            }
        }
    }

}