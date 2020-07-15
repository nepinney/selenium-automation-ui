package acquire.recoup.automatic.interfaces

import acquire.BackButton
import acquire.recoup.components.AddressComponent
import acquire.recoup.components.TicketModel
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.*

class AddressInterface(private val ticketModel: TicketModel) : BorderPane() {

    private val rootPane = BorderPane()
    private val headerGrid = GridPane()
    private val buttonsBox = HBox()
    private val noteArea = TextArea()

    private val backBtn = BackButton("recoupScan")
    private val createWaybillBtn = Button("Create Waybill")

    private val addressComponent = AddressComponent(ticketModel.currentTicket)

    private fun configureRootGrid() {

        rootPane.top = addressComponent
        rootPane.center = noteArea
        BorderPane.setMargin(noteArea, Insets(5.0, 0.0, 0.0, 0.0))

        /*rootGrid.hgap = 5.0
        val col1 = ColumnConstraints()
        col1.percentWidth = 100.0
        rootGrid.columnConstraints.addAll(col1)

        val row1 = RowConstraints()
        row1.percentHeight = 50.0
        val row2 = RowConstraints()
        row2.percentHeight = 50.0
        rootGrid.rowConstraints.addAll(row1, row2)

        rootGrid.add(addressComponent, 0, 0)
        rootGrid.add(noteArea, 0, 1)*/

        //For vertical layout
        /*val col1 = ColumnConstraints()
        col1.percentWidth = 40.0
        val col2 = ColumnConstraints()
        col2.percentWidth = 60.0
        rootGrid.columnConstraints.addAll(col1, col2)

        val row1 = RowConstraints()
        row1.percentHeight = 100.0
        rootGrid.rowConstraints.add(row1)

        rootGrid.add(noteArea, 0, 0)
        rootGrid.add(addressComponent, 1, 0)*/
        //rootGrid.isGridLinesVisible = true

    }

    private fun configureLabelHeader() {
        headerGrid.add(Label("Client address information:"), 0, 0)
    }

    private fun configureButtonsBox() {
        buttonsBox.spacing = 5.0
        buttonsBox.children.addAll(createWaybillBtn, backBtn)
    }

    private fun configureTicketNotes() {
        //val noteArea = TextArea()
        ticketModel.currentTicket.addListener { p0, p1, p2 ->
            noteArea.text = "***FOR REFERENCE***\n" + p0.value.lastNote
        }
    }

    init {
        configureLabelHeader()
        configureTicketNotes()
        configureRootGrid()
        configureButtonsBox()

        this.top = headerGrid
        BorderPane.setMargin(headerGrid, Insets(5.0, 5.0, 0.0, 5.0))
        //this.top.maxWidth(10.0)
        this.center = rootPane
        BorderPane.setMargin(rootPane, Insets(5.0))
        this.bottom = buttonsBox
        BorderPane.setMargin(buttonsBox, Insets(0.0, 5.0, 5.0, 5.0))
    }

}