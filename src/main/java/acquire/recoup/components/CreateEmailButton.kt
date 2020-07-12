package acquire.recoup.components

import acquire.Config
import acquire.Ticket
import acquire.exception.TicketIsNullException
import acquire.recoup.RecoupNotesParser
import acquire.recoup.manual.ManualActionTypes
import javafx.beans.property.ObjectProperty
import javafx.scene.control.Button
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class CreateEmailButton(
        private val actionType: ManualActionTypes,
        private val ticket: ObjectProperty<Ticket>
) : Button("Create Email") {

    private fun generateEmailContent(emailTemplatePath: String): List<String> {
        val emailVariables = mapOf(
                Pair("<tsc>", RecoupNotesParser.tscNumber(ticket.value.notes)),
                Pair("<owner>", RecoupNotesParser.ownerName(ticket.value.notes)),
                Pair("<model>", RecoupNotesParser.deviceModel(ticket.value.notes)),
                Pair("<sn>", RecoupNotesParser.serialNumber(ticket.value.notes)),
                Pair("<address>", RecoupNotesParser.clientToUpdateAndValidate(ticket.value.notes)),
                Pair("<deviceL>", RecoupNotesParser.deviceType(ticket.value.notes).first().toLowerCase() + RecoupNotesParser.deviceType(ticket.value.notes).slice(1..RecoupNotesParser.deviceType(ticket.value.notes).lastIndex)),
                Pair("<device>", RecoupNotesParser.deviceType(ticket.value.notes))
                //Pair("<device>", t!!.device)
        )

        val emailTemplate = File(emailTemplatePath)
        var fileScanner = Scanner(emailTemplate).useDelimiter("---End")

        var formattedEmail = fileScanner.next()
        for (variable in emailVariables) {
            formattedEmail = formattedEmail.replace(variable.key, variable.value.toString())
        }
        fileScanner.close()
        fileScanner = Scanner(formattedEmail).useDelimiter("---")
        //println("-------------------------------------------------------------------------")
        //println("1: ${fileScanner.next()}")
        //println("2: ${fileScanner.next().trim()}")
        val emailComponents = listOf(fileScanner.next(), fileScanner.next().trim())
        fileScanner.close()
        return emailComponents
    }

    private fun buildEmail(email: List<String>) {

        val validRecipients = RecoupNotesParser.validUniqueEmailAddresses(ticket.value.notes)

        try {
            val message: Message = MimeMessage(Session.getInstance(System.getProperties()))

            message.subject = email.first()
            //message.setFrom(InternetAddress("nick.pinney@cgi.com"))
            message.setHeader("X-Unsent", "1")
            // create the message part
            val content = MimeBodyPart()
            // fill message
            content.setText(email.last())
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(content)

            when (actionType) {
                ManualActionTypes.CONFIRMATION -> {
                    message.setRecipients(
                            Message.RecipientType.TO, InternetAddress.parse(
                            if (validRecipients.contains(RecoupNotesParser.requestorEmail(ticket.value.notes)))
                                RecoupNotesParser.requestorEmail(ticket.value.notes)
                            else
                                validRecipients.first()
                    )
                    )

                    message.addRecipient(Message.RecipientType.CC, InternetAddress("john.sullivan@cgi.com"))
                    message.addRecipient(Message.RecipientType.CC, InternetAddress("sayan.sengupta@cgi.com"))
                    try {
                        // add attachments
                        val attachPart = MimeBodyPart()
                        val attachmentPath =
                                "${Config.readSaveBuiltExcelConfirmationLocation()}\\PC return ${RecoupNotesParser.ownerName(ticket.value.notes)}.xls"
                        attachPart.attachFile(attachmentPath)
                        multipart.addBodyPart(attachPart)
                    } catch (e: Exception) {
                        println("$e: No such file to attach to the email.")
                    }
                }
                else -> {
                    when (validRecipients.count()) {
                        1 -> {
                            message.setRecipients(
                                    Message.RecipientType.TO,
                                    InternetAddress.parse(validRecipients.first())
                            )
                        }
                        2 -> {
                            if (validRecipients.first().equals(RecoupNotesParser.primaryEmail(ticket.value.notes))) {
                                message.setRecipients(
                                        Message.RecipientType.TO,
                                        InternetAddress.parse(validRecipients.first())
                                )
                                message.setRecipients(
                                        Message.RecipientType.CC,
                                        InternetAddress.parse(validRecipients.last())
                                )
                            } else {
                                message.setRecipients(
                                        Message.RecipientType.CC,
                                        InternetAddress.parse(validRecipients.first())
                                )
                                message.setRecipients(
                                        Message.RecipientType.TO,
                                        InternetAddress.parse(validRecipients.last())
                                )
                            }
                        }
                        3 -> {
                            message.setRecipients(
                                    Message.RecipientType.TO,
                                    InternetAddress.parse(RecoupNotesParser.primaryEmail(ticket.value.notes))
                            )
                            message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(RecoupNotesParser.requestorEmail(ticket.value.notes))
                            )
                            message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(RecoupNotesParser.secondaryEmail(ticket.value.notes))
                            )
                        }
                    }
                }
            }
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
        //return Config.readSaveEmailLocation()
    }

    private fun launchOutlook() {
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

    init {
        this.onAction = javafx.event.EventHandler {
            try {
                if (ticket.value == null)
                    throw TicketIsNullException()
                when (actionType) {
                    ManualActionTypes.LOCALREQUEST -> {
                        //println("LOCALREQUEST EMAIL")
                        buildEmail(generateEmailContent(Config.readLocalRequestEmailLocation()))
                        launchOutlook()
                    }
                    ManualActionTypes.OUTSIDEREQUEST -> {
                        //println("OUTSIDEREQUEST EMAIL")
                        buildEmail(generateEmailContent(Config.readOutsideRequestEmailLocation()))
                        launchOutlook()
                    }
                    ManualActionTypes.CREATEWAYBILL -> {
                        buildEmail(generateEmailContent(Config.readConfirmShipmentEmailLocation()))
                        launchOutlook()
                    }
                    ManualActionTypes.CONFIRMATION -> {
                        //println("CONFIRMATION EMAIL")
                        buildEmail(generateEmailContent(Config.readConfirmationRequestEmailLocation()))
                        launchOutlook()
                    }
                }
            }
            catch (e: TicketIsNullException) {
                println(e)
            }
        }
    }

}