package acquire.kotlin

import acquire.kotlin.DriverFunctions.driver
import org.openqa.selenium.*
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @Author Nicholas Pinney
 */
object ITSMFunctions {


    private var searchForm: WebElement? = null
    private var action: Actions? = null
    private var statusReasonField: WebElement? = null
    private var resolutionField: WebElement? = null

    /**
     * Used to log into the ITSM webportal
     * @param usn Plaintext username for ITSM
     * @param psw Plaintext password for ITSM
     */
    fun login(usn: String?, psw: String?) {
        //Do the login process
        val username = driver!!.findElement(By.name("cgi_username"))
        //fill the username form
        username.sendKeys(usn)
        val password = driver!!.findElement(By.name("pwd"))
        //fill the password form
        password.sendKeys(psw)
        //select the login button to click to login
        val login = driver!!.findElement(By.name("login"))
        login.click()
        ScreenController.activateScene("taskSelectionScene")
    }

    /**
     * Clears the search textBox in the ITSM webPortal
     */
    private fun clearSearchForm() {
        searchForm!!.clear()
    }

    /**
     * Sends keys to the search box of ITSM and hit ENTER
     * @param orderNumber String to be searched
     * TODO: surround findElement by try catches and have it return boolean success
     */
    fun searchOrderNumber(orderNumber: String) {
        //Search for the order number
        if (searchForm == null) searchForm =
            driver!!.findElement(By.id("arid_WIN_0_302258625"))
        clearSearchForm()
        searchForm!!.sendKeys(orderNumber + Keys.ENTER)
    }

    /**
     * Selects the first element from a search
     * TODO: instantiate Action property at the top of file and have function throw error
     */
    fun selectSearchResult() {
        //Wait for the clickable table row to come up, then double click on it if it exists
        val firstResult = WebDriverWait(driver, 10)
            .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"T1000003952\"]/tbody/tr[2]")))
        if (action == null) action =
            Actions(driver)
        action!!.doubleClick(firstResult).perform()
    }

    fun getDeviceType(): String {
        val deviceTextBox = driver!!.findElement(By.id("arid_WIN_3_303497400"))
        return deviceTextBox.getAttribute("value")
    }

    /**
     * Extracts the notes from notes textBox of ticket as String
     * @param alreadyOpen specifies if a ticket is already open/infocue or not
     * @return String of notes
     */
    fun getNotesFromTicket(alreadyOpen: Boolean): String {
        //Once the ticket has been opened, get the information from the notes textArea

        //NEW IMPLEMENTATION
        val timeOut = if (alreadyOpen) 1 else 8
        var infoTextArea: WebElement? = null
        try {
            infoTextArea = WebDriverWait(driver, timeOut.toLong())
                .until(ExpectedConditions.elementToBeClickable(By.id("arid_WIN_4_1000000151")))
            println("Fetched notes successfully")
        } catch (e: Exception) {
            try {
                infoTextArea = WebDriverWait(driver, timeOut.toLong())
                    .until(ExpectedConditions.elementToBeClickable(By.id("arid_WIN_3_1000000151")))
                println("Fetched notes successfully")
            } catch (j: Exception) {
                println("Fetched notes failed")
            }
        }
        return (infoTextArea!!.getAttribute("value"))
    }

    /*Helper methods to update ticket*/
    fun setStatusField(status: String) {
        //if (statusField == null) {
        val statusField = try {
            driver!!.findElement(By.id("arid_WIN_3_7"))
        } catch (e: NoSuchElementException) {
            driver!!.findElement(By.id("arid_WIN_4_7"))
        }
        //}
        statusField!!.click()
        var element: WebElement? = null
        if (status == "pending") {
            //try {
                element = WebDriverWait(driver, 3)
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/div[2]/table/tbody/tr[4]/td[1]")))
/*            } catch (e: NoSuchElementException) {
                try {
                    element = WebDriverWait(driver, 3)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/div[2]/table/tbody/tr[4]/td[1]")))
                }
            }*/

        } else {
            try {
                element = WebDriverWait(driver, 3)
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/div[2]/table/tbody/tr[3]/td[1]")))
            } catch (e: TimeoutException) {
                try {
                    element = WebDriverWait(driver, 3)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[5]/div[2]/table/tbody/tr[3]/td[1]")))
                } catch (t: TimeoutException) {
                    println("No Pending box: $t")
                }
            }
        }
        element?.click()
    }

    fun setStatusReasonField() {
        statusReasonField = try {
            driver!!.findElement(By.id("arid_WIN_3_1000000881"))
        } catch (e: NoSuchElementException) {
            driver!!.findElement(By.id("arid_WIN_4_1000000881"))
        }
        statusReasonField!!.click()
        val localSiteActionRequiredField = WebDriverWait(driver, 3)
            .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/div[2]/table/tbody/tr[2]/td[1]")))
        //WebElement localSiteField = SeleniumHelp.driver.findElement(By.xpath(""));
        localSiteActionRequiredField.click()
    }

    //only needed if recoup confirmation
    fun setResolution(resolution: String) {
        resolutionField = try {
            driver!!.findElement(By.id("arid_WIN_3_1000000156"))
        } catch (e: NoSuchElementException) {
            driver!!.findElement(By.id("arid_WIN_4_1000000156"))
        }
        resolutionField!!.sendKeys(resolution)
    }

    private fun getFormattedDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("M/dd/yyyy hh:mm:ss aa")
        return formatter.format(date).replace(".", "").toUpperCase()
    }

    fun setTargetDate() {
        try {
            val targetDateField = driver!!.findElement(By.id("arid_WIN_4_1000005261"))
            targetDateField.sendKeys(getFormattedDate())
        }
        catch (e: NoSuchElementException) {
            try {
                val targetDateField = driver!!.findElement(By.id("arid_WIN_3_1000005261"))
                targetDateField.sendKeys(getFormattedDate())
            }
            catch(d: NoSuchElementException) {
                println("Targer Date field not found")
            }
        }
    }

    fun setNotes(note: String?) {
        var notesField: WebElement? = null
        try {
            notesField = driver!!.findElement(By.id("arid_WIN_4_304247080"))
        } catch (e: NoSuchElementException) {
            println("Caught no such field exception using id; trying xpath...")
            try {
                notesField =
                    driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div[4]/div[105]/div/div/div[2]/fieldset/div/div/div/div/div[3]/fieldset/div/div/fieldset[1]/div[2]/div/div/div[3]/fieldset/div/div[2]/textarea"))
            } catch (q: NoSuchElementException) {
                println("Caught no such field exception using xpath.")
            }
        }
        notesField?.sendKeys(note)
    }

    fun attachSpreadsheet() {
        try {
            val uploadField = driver!!.findElement(By.id("WIN_4_304247100"))
            uploadField.click()
            /*WebElement up2 = new WebDriverWait(driver, 3)
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/table/tbody/tr/td/form/table/tbody/tr/td[2]/input")));
            up2.click();*/
            //up2.sendKeys("C:\\Users\\nick.pinney\\Desktop\\PendingXpath.txt");
            //WebElement okButton = driver.findElement(By.xpath("//*[@id=\"PopupAttFooter\"]/a[1]"));
            //okButton.click();
        } catch (e: NoSuchElementException) {
            println("Counldn't get element: $e")
        } catch (e: Exception) {
            println("Caught error when sending keys for file upload: $e")
        }
    }

    fun sortTicketsBasedOnStatus() {
        //TODO: Below commented code works
        val status = driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div/div[1]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div[2]/div/div[1]/div[6]"))
        status.click()
    }

    fun clickOnBackButton() {
        //val backBtn = driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[2]/fieldset/div/div/div/div/div[2]/fieldset/div/a[1]/div"))
        val backBtn = driver!!.findElement(By.id("WIN_0_304248620"))
        backBtn.click()
    }

    fun getIncidentID(): String {
        val idField = driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div[4]/div[105]/div/div/div[2]/fieldset/div/div/div/div/div[2]/fieldset/div/div/fieldset/div[1]/fieldset/div[1]/textarea"))
        return idField.getAttribute("value")
    }

    fun fetchAllInProgressTickets(): List<Ticket> {

/*        val status2 = driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div/div[1]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div[2]/div/div[2]/table/tbody/tr[2]/td[6]/nobr/span"))
        println("Status of first ticket meth1: ${status2.getAttribute("textContent")}")
        status2.click()*/

        //WebDriverWait(driver, 5).until { webDriver: WebDriver -> (webDriver as JavascriptExecutor).executeScript("return document.readyState") == "complete" }
        //driver!!.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS)
        var i = 2
        var tempINC: String? = null

        val shortWait = WebDriverWait(driver, 3.toLong())
        var tickets = mutableListOf<Ticket>()
        try {
            while (tickets.count() < 5) {
                println("\nTicket $i")
                //val aTicket = driver!!.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div/div[1]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div[2]/div/div[2]/table/tbody/tr[$i]/td[6]/nobr/span"))
                var aTicket: WebElement? = null
                try {
                    aTicket = WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div/div/div[3]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div/div[1]/fieldset/div/div/div/div/div[2]/fieldset/div/div/div/div[4]/div[2]/div/div/div[2]/fieldset/div/div/div[2]/div/div[2]/table/tbody/tr[$i]/td[6]/nobr/span")))
                } catch (n: NoSuchElementException) { throw Exception() }
                if (aTicket!!.getAttribute("textContent") == "In Progress") {
                    println("\tDouble Clicking...")
                    val dClick = Actions(driver)
                    dClick.doubleClick(aTicket!!).perform()
                    //println("\tWaiting...")
                    //WebDriverWait(driver, 5).until { webDriver: WebDriver -> (webDriver as JavascriptExecutor).executeScript("return document.readyState") == "complete" }
                    /*if (tempINC != null) {
                        WebDriverWait(driver, 5).until { webDriver: WebDriver -> (webDriver as JavascriptExecutor).executeScript("return document.readyState") == "complete" }
                        println("\nAfter double clicking second ticket incident ID is ${getIncidentID()} and tempINC is $tempINC")
                        *//*try {
                            shortWait.until(ExpectedCondition { getIncidentID() != tempINC.toString() } as ExpectedCondition<Boolean>)!!
                        } catch (e: Exception) { println("Waited too long for ticket to refresh.") }*//*
                    }*/
                    println("\tFetching notes and adding to array...")
                    tickets.add(0, Ticket(getNotesFromTicket(false)))
                    //tempINC = getIncidentID()
                    //println("Temp INC: $tempINC")
                    println("\tClicking on back button...")
                    clickOnBackButton()
                }
                println("\tIncrementing index...")
                i++
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("No more tickets to scan.")
        }
        return tickets
    }

    fun logout() {
/*        WebElement logoutLink = driver.findElement(By.id("WIN_0_300000044"));
        logoutLink.click();*/
    }

    fun exitDriver() {
        driver!!.quit()
        logout()
        try {
            Thread.sleep(800)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        println("Quit driver")
    }
}