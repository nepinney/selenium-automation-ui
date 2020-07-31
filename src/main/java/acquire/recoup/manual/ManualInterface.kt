package acquire.recoup.manual

import acquire.BackButton
import acquire.recoup.components.PrintTicketButton
import acquire.recoup.TicketModel
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox

class ManualInterface(
    private val menuFrame: ManualMenuComponent
) : AnchorPane() {

    private val splitRoot = SplitPane()

    private val ticketModel = TicketModel()

    private val localRequest = LocalRequest(ticketModel)
    private val outsideRequest = OutsideRequest(ticketModel)
    private val createWaybill = CreateWaybill(ticketModel)
    private val confirmation = Confirmation(ticketModel)

    private fun selectLocalRequest() {
        splitRoot.items.add(0, localRequest)
        //println("LocalRequest")
    }

    private fun selectOutsideRequest() {
        splitRoot.items.add(0, outsideRequest)
        //println("OutsideRequest")
    }

    private fun selectCreateWaybill() {
        splitRoot.items.add(0, createWaybill)
        //println("CreateWaybill")
    }

    private fun selectConfirmation() {
        splitRoot.items.add(0, confirmation)
        //println("Confirmation")
    }

    private fun configureSplitRoot() {
        AnchorPane.setBottomAnchor(splitRoot, 0.0)
        AnchorPane.setLeftAnchor(splitRoot, 0.0)
        AnchorPane.setRightAnchor(splitRoot, 0.0)
        AnchorPane.setTopAnchor(splitRoot, 0.0)

        splitRoot.items.add(menuFrame)
        splitRoot.orientation = Orientation.VERTICAL
        SplitPane.setResizableWithParent(menuFrame, true)
    }

    init {
        configureSplitRoot()
        this.children.add(splitRoot)

        val buttonHbox1 = HBox()
        buttonHbox1.spacing = 5.0
        buttonHbox1.children.addAll(PrintTicketButton(ticketModel.currentTicket), ManuallnstructionsButton(), BackButton("taskSelectionScene"))
        val buttonHbox2 = HBox()
        buttonHbox2.spacing = 5.0
        buttonHbox2.children.addAll(PrintTicketButton(ticketModel.currentTicket), ManuallnstructionsButton(), BackButton("taskSelectionScene"))
        val buttonHbox3 = HBox()
        buttonHbox3.spacing = 5.0
        buttonHbox3.children.addAll(PrintTicketButton(ticketModel.currentTicket), ManuallnstructionsButton(), BackButton("taskSelectionScene"))
        val buttonHbox4 = HBox()
        buttonHbox4.spacing = 5.0
        buttonHbox4.children.addAll(PrintTicketButton(ticketModel.currentTicket), ManuallnstructionsButton(), BackButton("taskSelectionScene"))

        localRequest.children.add(buttonHbox1)
        outsideRequest.children.add(buttonHbox2)
        createWaybill.children.add(buttonHbox3)
        confirmation.children.add(buttonHbox4)

        //localRequest.children.addAll(PrintTicketButton(ticketModel.currentTicket), BackButton())
        //outsideRequest.children.addAll(PrintTicketButton(ticketModel.currentTicket), BackButton())
        //createWaybill.children.addAll(PrintTicketButton(ticketModel.currentTicket), BackButton())
        //confirmation.children.addAll(PrintTicketButton(ticketModel.currentTicket), BackButton())

        this.selectLocalRequest()

        menuFrame.selectionModeProperty()
            .addListener { _: ObservableValue<out ManualActionTypes>?, oldValue: ManualActionTypes, newValue: ManualActionTypes ->
                when (oldValue) {
                    ManualActionTypes.LOCALREQUEST -> { splitRoot.items.remove(localRequest) }
                    ManualActionTypes.OUTSIDEREQUEST -> { splitRoot.items.remove(outsideRequest) }
                    ManualActionTypes.CREATEWAYBILL -> { splitRoot.items.remove(createWaybill) }
                    ManualActionTypes.CONFIRMATION -> { splitRoot.items.remove(confirmation) }
                }
                when (newValue) {
                    ManualActionTypes.LOCALREQUEST -> { selectLocalRequest() }
                    ManualActionTypes.OUTSIDEREQUEST -> { selectOutsideRequest() }
                    ManualActionTypes.CREATEWAYBILL -> { selectCreateWaybill() }
                    ManualActionTypes.CONFIRMATION -> { selectConfirmation() }
                }
                //splitLayout.setDividerPosition(0, DIVIDER_POSITION)
            }

    }

}