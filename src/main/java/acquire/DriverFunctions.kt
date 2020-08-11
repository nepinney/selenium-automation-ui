package acquire

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import io.github.bonigarcia.wdm.WebDriverManager
import kotlin.collections.ArrayList

object DriverFunctions {

    var driver: WebDriver? = null
    var isRunning = false
    var tabs: MutableMap<String, String>? = null

    /**
     * Starts the ChromeDriver and sets the isRunning property to true
     */
    fun createDriver() {
        WebDriverManager.chromedriver().setup()
        //System.setProperty("webdriver.chrome.driver", Config.readChromeDriverLocation())
        driver = ChromeDriver()
        //val tabHandles = driver!!.windowHandles.toList()
        //tabs = mutableMapOf(Pair(tabHandles.first(), "itsm"))
        isRunning = true
    }

    fun createNewTab(link: String, key: String) {
        switchToTab("itsm")
        when (isRunning) {
            true -> {
                (driver as JavascriptExecutor).executeScript("window.open()")
                val handles = driver!!.windowHandles.toList()
                if (tabs == null) {
                    tabs = mutableMapOf(
                            Pair("itsm", handles.first()),
                            Pair(key, handles.last())
                    )
                }
                else { //this logic not needed now as no other tabs will be opened
                    println("tabs is not null")
                }

                //tabs!![handles.last()] = key
                //driver!!.switchTo().window(tabs!![key])
                switchToTab(key)
                navigateToSite(link)
            }
            else -> {
                println("Driver isn't running!")
            }
        }

    }

    fun navigateToSite(link: String) {
        driver!![link]
    }

    fun switchToTab(key: String) {
        when (isRunning && tabs != null) {
            true -> {
                driver!!.switchTo().window(tabs!![key])
            }
        }
    }

    fun closeTab(tabHandle: String) {
        switchToTab(tabHandle)
        (driver as JavascriptExecutor).executeScript("window.close()")
        tabs?.remove(tabHandle)
        switchToTab("itsm")
        driver!!.switchTo().defaultContent()
        tabs = null
    }

}