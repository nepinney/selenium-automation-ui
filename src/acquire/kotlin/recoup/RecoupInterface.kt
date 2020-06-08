package acquire.kotlin.recoup

import acquire.kotlin.MenuFrame
import acquire.kotlin.recoup.action.Confirmation
import acquire.kotlin.recoup.action.CreateWaybill
import acquire.kotlin.recoup.action.LocalRequest
import acquire.kotlin.recoup.action.OutsideRequest
import acquire.kotlin.recoup.component.PrintTicketButton
import acquire.kotlin.recoup.component.TicketModel
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.AnchorPane
import java.io.IOException

class RecoupInterface(
    private val menuFrame: MenuFrame
) : AnchorPane() {

    private val splitRoot = SplitPane()

    private val ticketModel = TicketModel()

    private val localRequest = LocalRequest(ticketModel)
    private val outsideRequest = OutsideRequest(ticketModel)
    private val createWaybill = CreateWaybill(ticketModel)
    private val confirmation = Confirmation(ticketModel)
    private val printTicketButton = PrintTicketButton(ticketModel.currentTicket)

    private fun selectLocalRequest() {
        splitRoot.items.add(0, localRequest)
        println("LocalRequest")
    }

    private fun selectOutsideRequest() {
        splitRoot.items.add(0, outsideRequest)
        println("OutsideRequest")
    }

    private fun selectCreateWaybill() {
        splitRoot.items.add(0, createWaybill)
        println("CreateWaybill")
    }

    private fun selectConfirmation() {
        splitRoot.items.add(0, confirmation)
        println("Confirmation")
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

        localRequest.children.add(PrintTicketButton(ticketModel.currentTicket))
        outsideRequest.children.add(PrintTicketButton(ticketModel.currentTicket))
        createWaybill.children.add(PrintTicketButton(ticketModel.currentTicket))
        confirmation.children.add(PrintTicketButton(ticketModel.currentTicket))

        this.selectLocalRequest()

        menuFrame.selectionModeProperty()
            .addListener { _: ObservableValue<out RecoupActionTypes>?, oldValue: RecoupActionTypes, newValue: RecoupActionTypes ->
                when (oldValue) {
                    RecoupActionTypes.LOCALREQUEST -> { splitRoot.items.remove(localRequest) }
                    RecoupActionTypes.OUTSIDEREQUEST -> { splitRoot.items.remove(outsideRequest) }
                    RecoupActionTypes.CREATEWAYBILL -> { splitRoot.items.remove(createWaybill) }
                    RecoupActionTypes.CONFIRMATION -> { splitRoot.items.remove(confirmation) }
                }
                when (newValue) {
                    RecoupActionTypes.LOCALREQUEST -> { selectLocalRequest() }
                    RecoupActionTypes.OUTSIDEREQUEST -> { selectOutsideRequest() }
                    RecoupActionTypes.CREATEWAYBILL -> { selectCreateWaybill() }
                    RecoupActionTypes.CONFIRMATION -> { selectConfirmation() }
                }
                //splitLayout.setDividerPosition(0, DIVIDER_POSITION)
            }

    }

}