package acquire

import java.io.File

/**
 * @Author Nicholas Pinney
 */
object Config {

    private val workingDirectoryFile = File(
        Main::class.java.protectionDomain.codeSource.location
            .toURI()
    ).path.toString()
    val workingDirectory = workingDirectoryFile.slice(0..workingDirectoryFile.lastIndexOf('\\'))

    private val configFile = File("${workingDirectory}\\config.txt") //in same package so when building jar, jar and config are in same directory
    //private val configFile = File("$workingDirectory\\config.txt") //for running and building in editor uncomment this line
    private val configText = configFile.readText()

    /*
     *Files in config.txt that only have the file name must be prepended with the workingDirectory path on return
     */
    fun readLocalRequestEmailLocation(): String {
        val id = "localEmailTemplate:"
        return workingDirectory + read(id) //good
    }

    fun readOutsideRequestEmailLocation(): String {
        val id = "outsideEmailTemplate:"
        return workingDirectory + read(id) //good
    }

    fun readConfirmShipmentEmailLocation(): String {
        val id = "confirmShipmentTemplate:"
        return workingDirectory + read(id)
    }

    fun readConfirmationRequestEmailLocation(): String {
        val id = "confirmationEmailTemplate:"
        return workingDirectory + read(id) //good
    }

    fun readExcelConfirmationTemplateLocation(): String {
        val id = "excelConfirmationTemplate:"
        return "${workingDirectory}${read(id)}" //good
    }

    fun readChromeDriverLocation(): String {
        val id = "chromeDriverLocation:"
        return workingDirectory + read(id) //good
    }

    /*
     * The name of the saved file is specified here
     */
    fun readSaveEmailLocation(): String {
        val id = "saveBuiltEmailLocation:"
        return "${interpret(read(id))}createdEmail.eml"
    }

    fun readSaveBuiltExcelConfirmationLocation(): String {
        val id = "saveBuiltExcelConfirmation:"
        return interpret(read(id))
    }

    fun readPathToOutlookExe(): String {
        val id = "pathToOutlookExe:"
        return read(id)
    }

    fun readPathToCredFile(): String {
        val id = "pathToCredFile:"
        return "${workingDirectory}${read(id)}"
    }

    fun readPathToTicketAnalysisTemplate(): String {
        val id = "scanTicketsExcelTemplate:"
        return "${workingDirectory}${read(id)}"
    }

    fun readPathToTicketAnalysisSheet(): String {
        val id = "ticketsAnalysisExcelSheet:"
        return "${workingDirectory}${read(id)}"
    }

    private fun interpret(path: String): String {
        if (path.isBlank()) {
            return workingDirectory
        }
        return path
    }

    private fun read(id: String): String {
        return configText.slice(configText.indexOf(id)+id.length until configText.indexOf("\n", configText.indexOf(id))).trim()
    }
}