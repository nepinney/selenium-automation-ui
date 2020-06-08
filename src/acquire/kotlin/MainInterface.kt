package acquire.kotlin

import acquire.kotlin.recoup.RecoupActionTypes
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.SplitPane
import javafx.scene.layout.AnchorPane
import java.io.IOException

/**
 * @Author Nicholas Pinney
 */
class MainInterface internal constructor() : AnchorPane() {

    private val DIVIDER_POSITION = 0.13

    @FXML private lateinit var splitLayout: SplitPane
    @FXML private lateinit var menuFrame: acquire.kotlin.MenuFrame
    private val actionFrame = acquire.kotlin.ActionFrame()

    private fun addActionFrameToSplitLayout() {
        splitLayout.items.add(0, actionFrame)
        SplitPane.setResizableWithParent(actionFrame, true)
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/main_interface.fxml"))
        fxmlLoader.classLoader = javaClass.classLoader
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
            exception.printStackTrace()
        }
        splitLayout.setDividerPosition(0, DIVIDER_POSITION)
        addActionFrameToSplitLayout()

        actionFrame.actionType = RecoupActionTypes.LOCALREQUEST
        menuFrame.selectionModeProperty()
            .addListener { _: ObservableValue<out RecoupActionTypes>?, oldValue: RecoupActionTypes, newValue: RecoupActionTypes ->
                // write your code here
                //println("The:$newValue is selected")
                actionFrame.actionType = newValue
                when (newValue) {
                    RecoupActionTypes.CONFIRMATION -> {
                        if (oldValue == RecoupActionTypes.CREATEWAYBILL) actionFrame.removeCreateWaybillChanges()
                        actionFrame.alterLayoutForConfirmation()
                    }
                    RecoupActionTypes.CREATEWAYBILL -> {
                        if (oldValue == RecoupActionTypes.CONFIRMATION) actionFrame.removeConfirmationChanges()
                        actionFrame.alterLayoutForCreateWaybill()
                    }
                    //ActionTypes.OUTSIDEREQUEST -> { actionFrame.alterLayoutForOutsideRequest() }
                    else -> {
                        if (oldValue == RecoupActionTypes.CONFIRMATION) actionFrame.removeConfirmationChanges()
                        if (oldValue == RecoupActionTypes.CREATEWAYBILL) actionFrame.removeCreateWaybillChanges()
                    }
                }
                //splitLayout.setDividerPosition(0, DIVIDER_POSITION)
            }
    }
}
