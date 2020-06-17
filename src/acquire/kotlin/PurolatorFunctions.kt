package acquire.kotlin

import acquire.kotlin.SeleniumMethods.driver
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.*
import kotlin.math.log


object PurolatorFunctions {

    fun openPurolator() {
        if (driver == null)
            println("Driver is not running")
        else {
            (driver as JavascriptExecutor).executeScript("window.open()")
            val tabs = ArrayList(driver!!.windowHandles)
            driver!!.switchTo().window(tabs[1])
            driver!!.get("https://eshiponline.purolator.com/ShipOnline/SecurePages/Public/FormsLogin.aspx?ReturnUrl=/ShipOnline/Welcome.aspx&lang=E")
            /*val command = Keys.chord(Keys.CONTROL, "t")
            driver!!.findElement(By.cssSelector("body")).sendKeys(command)*/
        }
    }

    fun login(usr: String, ps: String) {
        val usernameField = driver!!.findElement(By.id("ctl00_Login_TxtBoxUserName"))
        usernameField.sendKeys(usr)
        val passwordField = driver!!.findElement(By.id("ctl00_Login_TxtBoxPassword"))
        passwordField.sendKeys(ps)
        val loginButton = driver!!.findElement(By.id("ctl00_Login_BtnLogin"))
        loginButton.click()
    }

    fun createShipmentFromHome() {
        val createNewLabelLink = WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPPC_btnCreateShipment")))
        createNewLabelLink.click()
    }

    fun enterShippingInformation(atnTo: String, pc: String, stNumber: String, stName: String, phoneArea: String, phone: String) {
        val company = WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPPC_ToAd_txtName")))
        company.sendKeys("Bell")

        val attentionTo = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtAttention"))
        attentionTo.sendKeys(atnTo)

        val postalCode = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtPostalZipCode"))
        postalCode.sendKeys(pc)

        /*
        Not going to do CITY!!!
        val cityField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtCity"))
        if (cityField.text.isBlank())
            cityField.sendKeys(city)*/

        val streetNumberField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetNumber"))
        println("The street number field is blank: ${streetNumberField.text.isBlank()}")
        if (streetNumberField.text.isBlank()) {
            streetNumberField.sendKeys(stNumber)
        }

        val streetNameField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetName"))
        println("The street name field is blank: ${streetNameField.text.isBlank()}")
        if (!streetNameField.text.isBlank()) {
            streetNameField.sendKeys(stName)
        }

        val phoneAreaField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtPhoneArea"))
        phoneAreaField.sendKeys(phoneArea)

        val phoneField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtPhone"))
        phoneField.sendKeys(phone)
    }

    fun selectDropOff() {
        val dropOffRadio = driver!!.findElement(By.id("ctl00_CPPC_pickupCtrl_rdoDropOff"))
        dropOffRadio.click()
    }
}