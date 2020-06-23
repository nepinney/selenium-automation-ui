package acquire.kotlin

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import kotlin.collections.ArrayList

object DriverFunctions {

    var driver: WebDriver? = null
    var isRunning = false
    var tabs: MutableMap<String, String>? = null

    /**
     * Starts the ChromeDriver and sets the isRunning property to true
     */
    fun createDriver() {
        System.setProperty("webdriver.chrome.driver", Config.readChromeDriverLocation())
        driver = ChromeDriver()
        tabs = driver!!.windowHandles.toList()
        isRunning = true
    }

    fun createNewTab(link: String) {
        when (isRunning) {
            true -> {
                (driver as JavascriptExecutor).executeScript("window.open()")
                if (tabs == null) {
                    val handles = ArrayList(driver!!.windowHandles)
                    tabs = listOf("itsm", "purolator").zip(handles)
                }
                driver!!.switchTo().window(tabs!!.last())
                driver!!.get(link)
            }
            else -> {
                println("Driver isn't running!")
            }
        }

    }

}