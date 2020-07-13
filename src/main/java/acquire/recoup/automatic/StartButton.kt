package acquire.recoup.automatic

import acquire.ITSMFunctions
import acquire.Ticket
import acquire.recoup.components.TicketModel
import javafx.scene.control.Button

class StartButton(
        private val ticketModel: TicketModel
) : Button("Start") {

    init {
        this.onAction = javafx.event.EventHandler {
        /*val ticket = Ticket("Submit\n" +
            "ITEM DETAILS\n" +
            "Order number : 200528584702\n" +
            "Order date : 2020/05/28 10:01:43 AM\n" +
            "Requestor\n" +
            "Shuo Wang (6085588)\tEmail : SHUO.WANG@BELL.CA\n" +
            "Telephone\t(416) 353-4120\tCompany\tBELL MOBILITY (10)\n" +
            "Owner\n" +
            "Denisa Bani (EZ76005)\tEmail : DENISA.BANI@BELL.CA\n" +
            "Telephone\t\tCompany\tBELL MOBILITY(10)\n" +
            "Org. Code\tH8041051\tProvince\tOntario\n" +
            "Title\tINTERN, DATA SCIENCE\tBuilding code\tTOROONDC\n" +
            "Item summary\n" +
            "Denisa Bani (EZ76005)\n" +
            "Product\tAgent\tStatus\n" +
            "Return to Reuse Bell - Computer or Thin Client\tCGI DSM North York\tAssigned\n" +
            "Item details\n" +
            "Denisa Bani (EZ76005)\n" +
            "200528-5847-0201\tReturn to Reuse Bell - Computer or Thin Client\n" +
            "Status\tAssigned\n" +
            "Agent\tCGI DSM North York\n" +
            "Billing Reference Number\t001131769\n" +
            "Broad Service Category\tDisconnect\n" +
            "Contact Information\tDsm North York (DSNYRK)\n" +
            "Services - Entity - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Entity\tBell TV - Bell Mobility - Bell Mobility Channels - BDI\n" +
            "Configuration - Usage - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Usage\tPrimary : Sole and exclusive station for main job functions by an active employee.\n" +
            "Configuration - Return - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "A separate item is required per computer to manage inventories.\tLaptop\n" +
            "Configuration - Details - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Computer, peripherals, accessories and/or videozone devices to be returned\tLaptop\n" +
            "Carrying Case\n" +
            "AC Adapter\n" +
            "Please enter the quantities for each computers, peripherals, accessories and devices to be returned\t1,1,1\n" +
            "Please validate and update\tDenisa Bani\n" +
            "11 Orlando Blvd -\n" +
            "Toronto - ON M1R 3N5\n" +
            "Exact location: suite, room, section, column, cubicle, extension, etc.\tN/A\n" +
            "Configuration - Method - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "You may return the computer and accessories to a DEPOT or request a pickup.\tPlease arrange for a pick-up.\n" +
            "Your answer will determine which group will handle this request.\tON North York - 100 Wynford\n" +
            "Configuration - Contacts - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Primary (on-site)\tShuo Wang (6085588) - (416) 353-4120 - SHUO.WANG@BELL.CA\n" +
            "Secondary (on-site)\tJesung Park (6065773) - (416) 353-3987 - JESUNG.PARK@BELL.CA\n" +
            "Billing, Shipping & Installation - Desired Due Date - Computer Name or Serial # (in Upper Case):HP PROBOOK 640 G35CG74423NL (Return to Reuse Bell - Computer or Thin Client) - 20052858470201\n" +
            "Desired Due Date\t2020/05/29", "1")*/



            //var ticketIndex = 2 //Start of tickets
            println("Fetching next ticket from index: ${ticketModel.currentTicket.value.ticketIndex}")
            when (ticketModel.currentTicket.value.ticketIndex!! > 2) {
                true -> { ITSMFunctions.clickOnBackButton() }
                false -> { }
            }

            var ticket: Ticket? = ITSMFunctions.fetchNextActiveTicket(ticketModel.currentTicket.value.ticketIndex!!)
            if (ticket != null) {
                ticketModel.setCurrentTicket(ticket)
                this.text = "Next"
            }
            else {
                println("No more tickets!")
                this.text = "Start"
            }

/*            while (nextTicketReady) {
                val indexAndTicket = ITSMFunctions.fetchNextActiveTicket(ticketIndex)
                ticketIndex = indexAndTicket.first
                ticket = indexAndTicket.second
                if (ticket != null) {
                    ticketModel.setCurrentTicket(ticket)
                }

            }*/
            //fetch ticket

            //Display the interface
                //interface will do the tasks on current ticket

            //fetch next ticket

            //ticketModel.setCurrentTicket(ticket)
            //println("Starting automatic process...")
        }
    }

}