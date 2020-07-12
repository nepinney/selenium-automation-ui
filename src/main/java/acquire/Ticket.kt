package acquire

/**
 * @Author Nicholas Pinney
 */
data class Ticket (
        val notes: String,
        val incNumber: String,
        val lastModified: String? = null,
        val ticketType: String? = null) {
}

