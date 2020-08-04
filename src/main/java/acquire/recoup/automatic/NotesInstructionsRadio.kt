package acquire.recoup.automatic

import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.Callable

class NotesInstructionsRadio : HBox(), Initializable {

    val displayType: ObjectProperty<TicketDisplayType> = SimpleObjectProperty<TicketDisplayType>(this, "displayType")

    @FXML
    lateinit var showNote: RadioButton
    @FXML
    lateinit var showInstruction: RadioButton
    @FXML
    lateinit var selection: ToggleGroup

    override fun initialize(url: URL, resources: ResourceBundle?) {
        showNote.userData = TicketDisplayType.LATESTNOTE
        showInstruction.userData = TicketDisplayType.INSTRUCTIONS

        displayType.bind(Bindings.createObjectBinding(Callable {
            val selectedToggle = selection.selectedToggle
            selectedToggle.userData as TicketDisplayType
        }, selection.selectedToggleProperty()))
    }

    fun selectionModeProperty(): ObjectProperty<TicketDisplayType> {
        return displayType
    }

    fun getSelectionMode(): TicketDisplayType {
        return displayType.get()
    }

    init {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/automatic_display_types.fxml"))
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