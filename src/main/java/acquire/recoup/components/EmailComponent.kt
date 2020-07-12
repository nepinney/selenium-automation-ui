package acquire.recoup.components

import acquire.Ticket
import acquire.recoup.manual.ManualActionTypes
import javafx.beans.property.ObjectProperty
import javafx.geometry.HPos
import javafx.scene.control.Label
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane

class EmailComponent(
        private val labelText: String,
        private val ticket: ObjectProperty<Ticket>,
        private val actionType: ManualActionTypes
) : GridPane() {

    private val label = Label(labelText)
    private val createEmailButton = CreateEmailButton(actionType, ticket)

    init {
        this.add(label, 0, 0)
        this.add(createEmailButton, 1, 1)
        GridPane.setHalignment(createEmailButton, HPos.RIGHT)

        val col1 = ColumnConstraints()
        col1.percentWidth = 70.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 30.0
        this.columnConstraints.addAll(col1,col2)

    }

}