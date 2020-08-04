package acquire

import acquire.DriverFunctions.driver
import acquire.recoup.WaybillInformation
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


object PurolatorFunctions {

    var purolatorOpen = false

    fun switchToPurolatorTab() {
        DriverFunctions.switchToTab("purolator")
    }

    fun createPurolatorTab() {
        //Navigate to Purolator
        DriverFunctions.createNewTab("https://eshiponline.purolator.com/ShipOnline/SecurePages/Public/FormsLogin.aspx?ReturnUrl=/ShipOnline/Welcome.aspx&lang=E", "purolator")
        //Login
        login("jpaquete", "eusteam")
    }

    fun login(usr: String, ps: String) {
        val usernameField = driver!!.findElement(By.id("ctl00_Login_TxtBoxUserName"))
        usernameField.sendKeys(usr)
        val passwordField = driver!!.findElement(By.id("ctl00_Login_TxtBoxPassword"))
        passwordField.sendKeys(ps)
        val loginButton = driver!!.findElement(By.id("ctl00_Login_BtnLogin"))
        loginButton.click()
    }

    fun createNewShipment() {
        try{
            val homeBtn = driver!!.findElement(By.id("ctl00_PageHeader_lbHomeButton"))
            homeBtn.click()
            val btn = WebDriverWait(driver, 3)
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPPC_btnCreateShipment")))
            btn.click()
        } catch(e: NoSuchElementException){
            println("Create shipment failed")
        }
    }

    fun fillAddressFields(address: WaybillInformation) {
        try {
            //Fill fields
            fillCompanyName(address.company)
            //Fill atn to
            fillAttentionTo(address.atnTo)
            //fill ps code
            fillPostalCode(address.postalcode)
            //fill street #
            checkAndFillStreetNumber(address.streetNumber)
            //fill street name
            checkAndFillStreetName(address.streetName)
            //fill phone number
            fillPhoneNumber(address.phoneNumberArea, address.phoneNumber)
            selectDropOff()
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            println("\nCaught an error finding an element")
        }
    }

    private fun fillCompanyName(str: String) {
        val wait = WebDriverWait(driver, 4)
        val company = wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPPC_ToAd_txtName")))
        company.sendKeys(str)
    }

    fun fillAttentionTo(atnTo: String) {
        val attentionTo = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtAttention"))
        attentionTo.sendKeys(atnTo)
    }

    fun fillPostalCode(pcode: String) {
        val postalCode = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtPostalZipCode"))
        postalCode.sendKeys(pcode + Keys.TAB)
    }

    fun checkAndFillStreetNumber(stNumber: String) {
        val shortWait = WebDriverWait(driver, 0.5.toLong())
        try {
            shortWait.until(ExpectedCondition { driver: WebDriver? -> driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetNumber")).getAttribute("value").isNotEmpty() } )!!
        } catch (e: TimeoutException) {
            println("The street number field was blank.")
            val streetNumberField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetNumber"))
            streetNumberField.sendKeys(stNumber)
        }
    }

    fun checkAndFillStreetName(stName: String) {
        val shortWait = WebDriverWait(driver, 0.5.toLong())
        try {
            shortWait.until(ExpectedCondition { driver: WebDriver? -> driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetName")).getAttribute("value").isNotEmpty() } )!!
        } catch (e: TimeoutException) {
            println("The street name field was blank.")
            val streetNumberField = driver!!.findElement(By.id("ctl00_CPPC_ToAd_txtStreetName"))
            streetNumberField.sendKeys(stName)
        }
    }

    fun fillPhoneNumber(phoneArea: String, phone: String) {
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