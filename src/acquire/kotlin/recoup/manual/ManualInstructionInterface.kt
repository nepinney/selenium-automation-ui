package acquire.kotlin.recoup.manual

import acquire.kotlin.BackButton
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class ManualInstructionInterface : VBox() {

    init {
        this.spacing = 5.0
        this.children.addAll(Label("1. Local Recoup"), Text("Do some stuff"), BackButton("recoupInterface"))
    }
}