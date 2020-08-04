package acquire

import acquire.recoup.RecoupNotesParser
import acquire.recoup.TicketModel
import acquire.recoup.WaybillInformation
import acquire.recoup.automatic.buttongroups.ButtonGroups
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class Email {
    var recipientList: List<String>? = null
    var ccList: List<String>? = null
    var subject: String? = null
    var body: String? = null

    fun buildViaBGroup() {
        when (TicketModel.currentTicket.value.automaticButtonGroup) {
            ButtonGroups.ASSIGNEDLOCAL -> {
                generateSubjectAndBody(Config.readLocalRequestEmailLocation())
            }
            ButtonGroups.ASSIGNEDOUTSIDE -> {
                generateSubjectAndBody(Config.readOutsideRequestEmailLocation())
            }
            ButtonGroups.INPROGRESSLOCAL -> {
                generateSubjectAndBody(Config.readLocalReminderEmailLocation())
            }
            ButtonGroups.INPROGRESSOUTSIDE -> {
                generateSubjectAndBody(Config.readOutsideReminderEmailLocation())
            }
            else -> {
                listOf("Failed reading email")
            }
        }
        generateRecipientsAndCcs()
    }

    fun buildShipmentConfirmationEmail(address: WaybillInformation) {
        val emailVariables = mapOf(
                Pair("<address>", (address.streetNumber + " " + address.streetName)),
                Pair("<atnTo>", address.atnTo),
                Pair("<outbound#>", address.outboundPin),
                Pair("<inbound#>", address.inboundPin)
        )

        val emailTemplate = File(Config.readConfirmShipmentEmailLocation())
        body = emailTemplate.readText()

        for (variable in emailVariables) {
            body = body!!.replace(variable.key, variable.value.toString())
        }

    }

    fun generateSubjectAndBody(emailTemplatePath: String) {
        val emailVariables = mapOf(
                Pair("<tsc>", RecoupNotesParser.tscNumber(TicketModel.currentTicket.value.notes)),
                Pair("<owner>", RecoupNotesParser.ownerName(TicketModel.currentTicket.value.notes)),
                Pair("<model>", RecoupNotesParser.deviceModel(TicketModel.currentTicket.value.notes)),
                Pair("<sn>", RecoupNotesParser.serialNumber(TicketModel.currentTicket.value.notes)),
                Pair("<address>", RecoupNotesParser.clientToUpdateAndValidate(TicketModel.currentTicket.value.notes)),
                Pair("<deviceL>", RecoupNotesParser.deviceType(TicketModel.currentTicket.value.notes).first().toLowerCase() + RecoupNotesParser.deviceType(TicketModel.currentTicket.value.notes).slice(1..RecoupNotesParser.deviceType(TicketModel.currentTicket.value.notes).lastIndex)),
                Pair("<device>", RecoupNotesParser.deviceType(TicketModel.currentTicket.value.notes))
        )

        val emailTemplate = File(emailTemplatePath)
        var fileScanner = Scanner(emailTemplate).useDelimiter("---End")

        var formattedEmail = fileScanner.next()
        for (variable in emailVariables) {
            formattedEmail = formattedEmail.replace(variable.key, variable.value)
        }
        fileScanner.close()
        fileScanner = Scanner(formattedEmail).useDelimiter("---")
        //println("-------------------------------------------------------------------------")
        //println("1: ${fileScanner.next()}")
        //println("2: ${fileScanner.next().trim()}")
        subject = fileScanner.next()
        body = fileScanner.next().trim()
        fileScanner.close()
    }

    fun generateRecipientsAndCcs() {

        val validRecipients = RecoupNotesParser.validUniqueEmailAddresses(TicketModel.currentTicket.value.notes)

        when (validRecipients.count()) {
            1 -> {
                recipientList = listOf(validRecipients.first()!!)
                ccList = listOf()
            }
            2 -> {
                if (validRecipients.first().equals(RecoupNotesParser.primaryEmail(TicketModel.currentTicket.value.notes))) {
                    recipientList = listOf(validRecipients.first()!!)
                    ccList = listOf(validRecipients.last()!!)
                }
                else {
                    recipientList = listOf(validRecipients.last()!!)
                    ccList = listOf(validRecipients.first()!!)
                }
            }
            3 -> {
                recipientList = listOf(RecoupNotesParser.primaryEmail(TicketModel.currentTicket.value.notes))
                ccList = listOf(
                        RecoupNotesParser.requestorEmail(TicketModel.currentTicket.value.notes),
                        RecoupNotesParser.secondaryEmail(TicketModel.currentTicket.value.notes)
                )
            }
            else -> {
                recipientList = listOf("Null")
                ccList = listOf("Null")
            }
        }
    }

    fun buildEmlFile() {
        try {
            val message: Message = MimeMessage(Session.getInstance(System.getProperties()))

            message.subject = subject
            //message.setFrom(InternetAddress("nick.pinney@cgi.com"))
            message.setHeader("X-Unsent", "1")
            // create the message part
            val content = MimeBodyPart()
            // fill message
            content.setText(body)
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(content)

            recipientList?.forEach { message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(it)) }
            ccList?.forEach { message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(it)) }

            // integration
            message.setContent(multipart)
            // store file
            val out = FileOutputStream(File(Config.readSaveEmailLocation()))
            message.writeTo(out)
            out.close()
            println("\nEmail saved successfully to: ${Config.readSaveEmailLocation()}")
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun addMetadataAndCopyEmail(): String {
        var noteToAdd: String
        val date = Date()
        val formatter = SimpleDateFormat("MMMM dd, yyyy hh:mm aa")
        val cc = if (ccList != null) "Cc: ${ccList?.joinToString(";")}\n" else ""
        return "From: Pinney, Nicholas\n" +
                "Sent: ${formatter.format(date).toUpperCase().replace(".", "")}\n" +
                "To: ${recipientList!!.joinToString(";")}\n" +
                "$cc" +
                "Subject: ${subject}\n\n" +
                "${body}"
    }

    fun launchOutlook() {
        try {
            val command = File(Config.readPathToOutlookExe()).absolutePath.replace('\\','/' ) + " /eml " + File(Config.readSaveEmailLocation().toString()).absolutePath.replace('\\','/')
            println("Executed command: $command")

            val builder = ProcessBuilder(File(Config.readPathToOutlookExe()).absolutePath.replace('\\','/' ), "/eml", File(Config.readSaveEmailLocation().toString()).absolutePath.replace('\\','/') )
            val process = builder.start()
            val result = process.waitFor()
            println("Process exited with result $result")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}