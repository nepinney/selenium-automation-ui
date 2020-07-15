package acquire.recoup.automatic

import acquire.DriverFunctions
import acquire.PurolatorFunctions
import acquire.recoup.MailingAddress

object CreateWaybill {

    fun createWaybill(address: MailingAddress) {
        println("Creating Waybill")
        when (PurolatorFunctions.purolatorOpen) {
            true -> {
                DriverFunctions.switchToTab("purolator")
            }
            false -> {
                DriverFunctions.createNewTab("https://eshiponline.purolator.com/ShipOnline/SecurePages/Public/FormsLogin.aspx?ReturnUrl=/ShipOnline/Welcome.aspx&lang=E", "purolator")
                PurolatorFunctions.login("jpaquete", "eusteam")
                PurolatorFunctions.purolatorOpen = true
                PurolatorFunctions.clickCreateShipmentButtonFromHomeScreen()
                //PurolatorFunctions.enterShippingInformation("Nicholas Pinney","L6H 5T8", "2140", "Winding Woods Drive", "905", "2575603")
                PurolatorFunctions.selectDropOff()
            }
        }
    }

}