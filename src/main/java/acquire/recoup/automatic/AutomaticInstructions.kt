package acquire.recoup.automatic

import acquire.BackButton
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.text.Text

class AutomaticInstructions : GridPane() {
    private val label1 = Label("1. Scan Tickets Button")
    private val label1Instructions = Text("The \"Scan Tickets\" button will scan all the tickets in your queue which are In Progress and create an excel sheet with their status so you may quickly view which ones need to be sent reminders.")

    init {
        this.add(label1, 0, 0)
        this.add(label1Instructions, 0, 1)
        this.add(BackButton("recoupScan"), 0, 2)
    }
}