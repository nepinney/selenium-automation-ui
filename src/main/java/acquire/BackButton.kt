package acquire

import acquire.recoup.TicketModel
import javafx.scene.control.Button

class BackButton(
        private val previousScreen: String,
        private var removeScenes: List<SceneHandle>? = null,
        private var listenerObjects: List<ListenerHandle>? = null,
        private val resetTicket: Boolean = false
) : Button("Back") {

    init {
        this.onAction = javafx.event.EventHandler {
            ScreenController.activateScene(previousScreen)

            removeScenes?.forEach {
                it.removeChildScenes()
            }

            listenerObjects?.forEach {
                it.deactivateListeners()
            }

            if (resetTicket) TicketModel.setCurrentTicket(Ticket("", ""))
            System.gc()
        }
    }

}