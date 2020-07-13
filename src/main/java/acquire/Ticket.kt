package acquire

import acquire.recoup.automatic.ButtonGroups

/**
 * @Author Nicholas Pinney
 */
data class Ticket (
        val notes: String,
        val incNumber: String,
        val status: String? = null,
        val lastModified: String? = null,
        val ticketType: String? = null,
        val ticketIndex: Int? = null,
        val lastNote: String? = null,
        val automaticButtonGroup: ButtonGroups? = null) {
}

