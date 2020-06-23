package acquire.kotlin

import acquire.kotlin.DriverFunctions.driver
import okio.Timeout
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


object PurolatorFunctions {

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
        val wait = WebDriverWait(driver, 4)
        val company = wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPPC_ToAd_txtName")))
        company.sendKeys("Bell")

        val attentionTo = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtAttention"))
        attentionTo.sendKeys(atnTo)

        val postalCode = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtPostalZipCode"))
        postalCode.sendKeys(pc + Keys.TAB)


        /*
        Not going to do CITY!!!
        val cityField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtCity"))
        if (cityField.text.isBlank())
            cityField.sendKeys(city)*/
/*       val city =  WebDriverWait(driver, 10)
               .until(ExpectedConditions.invisibilityOfElementWithText(By.id("ctl00_CPPC_ToAd_txtCity"), ""))
        println("City: $city" )*/


        val shortWait = WebDriverWait(driver, 0.5.toLong())
        try {
            shortWait.until(ExpectedCondition { driver: WebDriver? -> driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetNumber")).getAttribute("value").isNotEmpty() } as ExpectedCondition<Boolean?>)!!
        } catch (e: TimeoutException) {
            println("The street number field was blank.")
            val streetNumberField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetNumber"))
            streetNumberField.sendKeys(stNumber)
        }

        try {
            shortWait.until(ExpectedCondition { driver: WebDriver? -> driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetName")).getAttribute("value").isNotEmpty() } as ExpectedCondition<Boolean?>)!!
        } catch (e: TimeoutException) {
            println("The street name field was blank.")
            val streetNumberField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetName"))
            streetNumberField.sendKeys(stName)
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