package acquire.recoup.automatic

import acquire.Email
import acquire.ITSMFunctions
import java.text.SimpleDateFormat
import java.util.*

object AddNote {

    fun processNote(email: Email?, note: String?, noteType: NoteType) {
        var noteToAdd: String
        when (noteType) {
            NoteType.EMAILNOTE -> {
                val date = Date()
                val formatter = SimpleDateFormat("MMMM dd, yyyy hh:mm aa")
                val cc = if (email!!.cc != null) "Cc: ${email!!.cc?.joinToString(";")}\n" else ""
                noteToAdd =
                        "From: Pinney, Nicholas\n" +
                        "Sent: ${formatter.format(date).toUpperCase().replace(".", "")}\n" +
                        "To: ${email!!.recipient.joinToString(";")}\n" +
                        "$cc" +
                        "Subject: ${email!!.subject}\n\n" +
                        "${email!!.body}"
            }
            NoteType.CUSTOMNOTE -> {
                noteToAdd = note!!
            }
        }
        println("\n$noteToAdd")
        //ITSMFunctions.setNotes(noteToAdd)
    }

}