package acquire.recoup

object RecoupNotesParser {

    /**
     * tscNumber
     */
    fun tscNumber(notes: String): String {
        return notes.slice(notes.indexOf("Order number : ") + "Order number : ".length until notes.indexOf('\n', notes.indexOf("Order number : "))).trim()
    }

    /**
     * ownerName
     */
    fun ownerName(notes: String): String {
        return notes.slice(notes.indexOf("Owner\n")+"Owner\n".length until notes.indexOf("\tEmail", notes.indexOf("Owner\n"))).trim()
    }

    private fun modelAndSerialNumber(notes: String): String {
        val indexOfStringOne = notes.indexOf("Computer Name or Serial #")
        val indexRightBeforeModel = notes.indexOf(":", indexOfStringOne)
        return notes.slice(indexRightBeforeModel+1 until notes.indexOf("(Return")).trim()
    }

    private fun getSerialNumberPrefix(notes: String): String {
        return when (true) {
            modelAndSerialNumber(notes).contains("MXL") -> { "MXL" }
            modelAndSerialNumber(notes).contains("2UA") -> { "2UA" }
            modelAndSerialNumber(notes).contains("5CG") -> { "5CG" }
            else -> { "Unknown" }
        }
    }

    /**
     * deviceType
     */
    fun deviceType(notes: String): String {
        return when (getSerialNumberPrefix(notes)) {
            "MXL" -> {
                "Desktop"
            }
            "2UA" -> {
                "Desktop"
            }
            "5CG" -> {
                "Laptop"
            }
            else -> {
                "Unknown"
            }
        }
    }

    /**
     * serialNumber
     */
    fun serialNumber(notes: String): String {
        return if (getSerialNumberPrefix(notes) != "Unknown")
            modelAndSerialNumber(notes).slice(modelAndSerialNumber(notes).indexOf(getSerialNumberPrefix(notes)) .. modelAndSerialNumber(notes).lastIndex)
        else
            "Unknown"
    }

    /**
     * model
     */
    fun deviceModel(notes: String): String {
        return if ((modelAndSerialNumber(notes).slice(0 until modelAndSerialNumber(notes).indexOf(serialNumber(notes)))).isBlank())
            ""
        else
            modelAndSerialNumber(notes).slice(0 until modelAndSerialNumber(notes).indexOf(serialNumber(notes)))
    }

    /**
     * quantityInstructions
     */
    fun quantityInstructions(notes: String): String {
        return notes.slice(
                notes.indexOf("Please enter the quantities for each computers, peripherals, accessories and devices to be returned")
                        +"Please enter the quantities for each computers, peripherals, accessories and devices to be returned".length
                        until
                        notes.indexOf("\nPlease validate")).trim()
    }

    /**
     * clientToUpdateAndValidate
     */
    fun clientToUpdateAndValidate(notes: String): String {
        return notes.slice(
                notes.indexOf("Please validate and update")+"Please validate and update".length
                        until
                        notes.indexOf("Exact location")).trim()
    }

    /**
     * exactLocation
     */
    fun exactLocation(notes: String): String {
        return notes.slice(
                notes.indexOf("Exact location: suite, room, section, column, cubicle, extension, etc.")+"Exact location: suite, room, section, column, cubicle, extension, etc.".length
                        until
                        notes.indexOf("Configuration", notes.indexOf("Exact location: suite, room, section, column, cubicle, extension, etc."))).trim()

    }

    /**
     * requstorEmail
     * Watch: the missing space after the colon of "Emial :()" indicated by the brackets could cause problem. Removed them because first letter of emial was being
     * chopped off. We'll see if this adds problems for other texts.
     */
    fun requestorEmail(notes: String): String {
        return notes.slice(
                notes.indexOf("Email :") +" Email :".length
                        until
                        notes.indexOf("Telephone")).trim()
    }

    /**
     * primaryEmail
     */
    fun primaryEmail(notes: String): String {
        val primaryContact = notes.slice(
                notes.indexOf("Primary (on-site)")
                        until //-> could throw index out of bounds exception start -1 if .. instead of until
                        notes.indexOf("\nSecondary")
        )
        return primaryContact.slice(primaryContact.lastIndexOf('-')+2 until primaryContact.length)
    }

    /**
     * secondaryEmail
     */
    fun secondaryEmail(notes: String): String {
        val preSecondary = "Secondary (on-site)"
        val secondaryContact = notes.slice(
                notes.indexOf(preSecondary)+preSecondary.length
                        until //-> could throw index out of bounds exception start -1 if .. instead of until
                        notes.indexOf("\nBilling", notes.indexOf(preSecondary)))
        return secondaryContact.slice(secondaryContact.lastIndexOf('-')+2 until secondaryContact.length).trim()
    }

    /**
    For address extraction, the below functions have been created
    */
    fun attentionTo(notes: String): String {
        return clientToUpdateAndValidate(notes).slice(0..clientToUpdateAndValidate(notes).indexOf('\n')).trim()
    }


    fun streetNumber(notes: String): String {
        val streetNumberEndIndex = clientToUpdateAndValidate(notes).indexOf(' ', clientToUpdateAndValidate(notes).indexOf('\n'))
        return clientToUpdateAndValidate(notes).slice(clientToUpdateAndValidate(notes).indexOf('\n')..streetNumberEndIndex).trim()
    }

    fun streetName(notes: String): String {
        val streetNumberEndIndex = clientToUpdateAndValidate(notes).indexOf(' ', clientToUpdateAndValidate(notes).indexOf('\n'))
        val streetNameEndIndex = clientToUpdateAndValidate(notes).indexOf('-', streetNumberEndIndex)
        return clientToUpdateAndValidate(notes).slice(streetNumberEndIndex until streetNameEndIndex).trim()
    }

    /*
    The may be floor number in between these two elements of the client to update and validate
    */

    fun city(notes: String): String {
        val streetNameEndIndex = clientToUpdateAndValidate(notes).indexOf('\n', clientToUpdateAndValidate(notes).indexOf(streetNumber(notes)))
        val cityNameEndIndex = clientToUpdateAndValidate(notes).indexOf('-', streetNameEndIndex)
        return clientToUpdateAndValidate(notes).slice(streetNameEndIndex until cityNameEndIndex).trim()
    }

    fun postalCode(notes: String): String {
        val cityEndIndex = clientToUpdateAndValidate(notes).indexOf('-', clientToUpdateAndValidate(notes).indexOf(city(notes)))
        val provinceEndIndex = clientToUpdateAndValidate(notes).indexOf(' ', cityEndIndex + 3)
        val postalCodeEndIndex = clientToUpdateAndValidate(notes).lastIndex
        return clientToUpdateAndValidate(notes).slice(provinceEndIndex..postalCodeEndIndex).trim()
    }

    fun validUniqueEmailAddresses(notes: String): List<String?> {
        return listOf(requestorEmail(notes), primaryEmail(notes), secondaryEmail(notes)).distinct()//.filter { !it.contains("@") }
    }

    fun printInfo(notes: String) {
        println("\n\n------------------------------------------")
        println("tsc: ${tscNumber(notes)}")
        println("Owner: ${ownerName(notes)}")
        println("Req: ${requestorEmail(notes)}")
        println("Primary: ${primaryEmail(notes)}")
        println("Secondary: ${secondaryEmail(notes)}")
        println("Model: ${deviceModel(notes)}")
        //println("Model & SN: $modelAndSerialNumber")
        println("SN: ${serialNumber(notes)}")
        println("---------\nClient to update and validate: ${"\n\t" + clientToUpdateAndValidate(notes).replace("\n", "\n\t")}")
        println("---------\nQuantity/Instructions: ${quantityInstructions(notes)}")
        println("---------\nExact location: ${exactLocation(notes).replace("\n", "\t\n")}")
    }

}