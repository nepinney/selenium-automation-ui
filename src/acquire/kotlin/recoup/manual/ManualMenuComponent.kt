package acquire.kotlin.recoup.manual

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.Callable

class ManualMenuComponent : GridPane(), Initializable {

    private val actionTypes = ReadOnlyObjectWrapper<ManualActionTypes>(this, "actionTypes")

    @FXML
    private lateinit var localRequest: RadioButton
    @FXML
    private lateinit var outsideRequest: RadioButton
    @FXML
    private lateinit var createWaybill: RadioButton
    @FXML
    private lateinit var confirmation: RadioButton
    @FXML
    private lateinit var menuSelection: ToggleGroup

    override fun initialize(url: URL, resources: ResourceBundle?) {
        localRequest.userData = ManualActionTypes.LOCALREQUEST
        outsideRequest.userData = ManualActionTypes.OUTSIDEREQUEST
        createWaybill.userData = ManualActionTypes.CREATEWAYBILL
        confirmation.userData = ManualActionTypes.CONFIRMATION
        actionTypes.bind(Bindings.createObjectBinding(Callable {
            val selectedToggle = menuSelection.selectedToggle
            selectedToggle.userData as ManualActionTypes
        }, menuSelection.selectedToggleProperty()))
    }

    fun selectionModeProperty(): ReadOnlyObjectProperty<ManualActionTypes> {
        return actionTypes.readOnlyProperty
    }

    private fun getSelectionMode(): ManualActionTypes {
        return actionTypes.get()
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/acquire/fxml/recoup_menu_component.fxml"))
        fxmlLoader.classLoader = javaClass.classLoader
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}