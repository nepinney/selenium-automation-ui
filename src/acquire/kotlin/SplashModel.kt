package acquire.kotlin

import acquire.kotlin.SeleniumMethods.createDriver
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * @Author Nicholas Pinney
 */
class SplashModel {
    private val loadingStatuses: ObservableList<String> = FXCollections.observableArrayList<String>()
    val currentStatus: ObjectProperty<String> = SimpleObjectProperty(null)

    fun addLoadingStatus(status: String) {
        loadingStatuses.add(0, status)
        setCurrentStatus(loadingStatuses[0])
    }

    fun currentStatusProperty(): ObjectProperty<String> {
        return currentStatus
    }

    fun getCurrentStatus(): String? {
        return currentStatus.get()
    }

    private fun setCurrentStatus(currentStatus: String) {
        this.currentStatus.set(currentStatus)
    }

    fun loadWebDriver() {
        createDriver()
    }

    fun navigateToBMC() {
        SeleniumMethods.driver!!["https://supportportal.cgi.com/arsys/shared/login.jsp?/arsys/"]
    }
}