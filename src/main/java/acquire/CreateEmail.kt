package acquire

import acquire.recoup.RecoupNotesParser
import acquire.recoup.components.TicketModel
import acquire.recoup.manual.ManualActionTypes
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

object CreateEmail {

    fun determineRecipients(ticket: TicketModel): List<List<String>> {

        val validRecipients = RecoupNotesParser.validUniqueEmailAddresses(ticket.currentTicket.value.notes)

        return when (validRecipients.count()) {
            1 -> {
/*                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(validRecipients.first())
                )*/
                listOf(listOf(validRecipients.first()!!))
            }
            2 -> {
                if (validRecipients.first().equals(RecoupNotesParser.primaryEmail(ticket.currentTicket.value.notes))) {
/*                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(validRecipients.first())
                    )
                    message.setRecipients(
                            Message.RecipientType.CC,
                            InternetAddress.parse(validRecipients.last())
                    )*/
                    listOf(listOf(validRecipients.first()!!), listOf(validRecipients.last()!!))
                } else {
/*                    message.setRecipients(
                            Message.RecipientType.CC,
                            InternetAddress.parse(validRecipients.first())
                    )
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(validRecipients.last())
                    )*/
                    listOf(listOf(validRecipients.last()!!), listOf(validRecipients.first()!!))
                }
            }
            3 -> {
/*                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(RecoupNotesParser.primaryEmail(ticket.currentTicket.value.notes))
                )
                message.setRecipients(
                        Message.RecipientType.CC,
                        InternetAddress.parse(RecoupNotesParser.requestorEmail(ticket.currentTicket.value.notes))
                )
                message.setRecipients(
                        Message.RecipientType.CC,
                        InternetAddress.parse(RecoupNotesParser.secondaryEmail(ticket.currentTicket.value.notes))
                )*/
                listOf(
                        listOf(RecoupNotesParser.primaryEmail(ticket.currentTicket.value.notes)),
                        listOf(
                                RecoupNotesParser.requestorEmail(ticket.currentTicket.value.notes),
                                RecoupNotesParser.secondaryEmail(ticket.currentTicket.value.notes)
                        )
                )
            }
            else -> {
                listOf(listOf("No Results Found"))
            }
        }
    }

    fun generateEmailSubjectAndBodyFromTemplate(emailTemplatePath: String, ticket: TicketModel): List<String> {
        val emailVariables = mapOf(
                Pair("<tsc>", RecoupNotesParser.tscNumber(ticket.currentTicket.value.notes)),
                Pair("<owner>", RecoupNotesParser.ownerName(ticket.currentTicket.value.notes)),
                Pair("<model>", RecoupNotesParser.deviceModel(ticket.currentTicket.value.notes)),
                Pair("<sn>", RecoupNotesParser.serialNumber(ticket.currentTicket.value.notes)),
                Pair("<address>", RecoupNotesParser.clientToUpdateAndValidate(ticket.currentTicket.value.notes)),
                Pair("<deviceL>", RecoupNotesParser.deviceType(ticket.currentTicket.value.notes).first().toLowerCase() + RecoupNotesParser.deviceType(ticket.currentTicket.value.notes).slice(1..RecoupNotesParser.deviceType(ticket.currentTicket.value.notes).lastIndex)),
                Pair("<device>", RecoupNotesParser.deviceType(ticket.currentTicket.value.notes))
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

    fun buildEmail(email: Email) {

        //val validRecipients = RecoupNotesParser.validUniqueEmailAddresses(ticket.currentTicket.value.notes)

        try {
            val message: Message = MimeMessage(Session.getInstance(System.getProperties()))

            message.subject = email.subject
            //message.setFrom(InternetAddress("nick.pinney@cgi.com"))
            message.setHeader("X-Unsent", "1")
            // create the message part
            val content = MimeBodyPart()
            // fill message
            content.setText(email.body)
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(content)

            email.recipient.forEach { message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(it)) }
            email.cc?.forEach { message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(it)) }

            /*when (actionType) {
                ManualActionTypes.CONFIRMATION -> {
                    message.setRecipients(
                            Message.RecipientType.TO, InternetAddress.parse(
                            if (validRecipients.contains(RecoupNotesParser.requestorEmail(ticket.currentTicket.value.notes)))
                                RecoupNotesParser.requestorEmail(ticket.currentTicket.value.notes)
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
                                "${Config.readSaveBuiltExcelConfirmationLocation()}\\PC return ${RecoupNotesParser.ownerName(ticket.currentTicket.value.notes)}.xls"
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
                            if (validRecipients.first().equals(RecoupNotesParser.primaryEmail(ticket.currentTicket.value.notes))) {
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
                                    InternetAddress.parse(RecoupNotesParser.primaryEmail(ticket.currentTicket.value.notes))
                            )
                            message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(RecoupNotesParser.requestorEmail(ticket.currentTicket.value.notes))
                            )
                            message.setRecipients(
                                    Message.RecipientType.CC,
                                    InternetAddress.parse(RecoupNotesParser.secondaryEmail(ticket.currentTicket.value.notes))
                            )
                        }
                    }
                }
            }*/
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