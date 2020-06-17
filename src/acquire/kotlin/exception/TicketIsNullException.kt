package acquire.kotlin.exception

/**
 * @Author Nicholas Pinney
 */
class TicketIsNullException : Exception() {
    private val error  = "Must generate Ticket before creating email"
    override fun toString(): String {
        return error
    }
}