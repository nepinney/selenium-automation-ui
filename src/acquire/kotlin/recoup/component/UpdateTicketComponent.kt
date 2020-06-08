package acquire.kotlin.recoup.component

import acquire.kotlin.recoup.RecoupActionTypes
import javafx.geometry.HPos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane

class UpdateTicketComponent(
        private val actionType: RecoupActionTypes
) : GridPane() {
    private val instructionLabel = Label("3. Update ticket status")
    private val updateButton = Button("Update Ticket")

    private fun configureUpdateComponent() {
        this.add(instructionLabel, 0, 0)
        this.add(updateButton, 1, 1)

        GridPane.setHalignment(updateButton, HPos.RIGHT)

        val col1 = ColumnConstraints()
        col1.percentWidth = 70.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 30.0
        this.columnConstraints.addAll(col1,col2)
    }

    init {
        configureUpdateComponent()
        updateButton.onAction = javafx.event.EventHandler {
            when (actionType) {
                RecoupActionTypes.LOCALREQUEST -> { println("UpdatedLocal/OutsideRequest") }
                RecoupActionTypes.CONFIRMATION -> { println("UpdatedConfirmation") }
                else -> {
                    println("No update instructions for: $actionType")
                }
            }
        }
    }
}