package acquire

data class Email(
        val recipient: List<String>,
        val cc: List<String>? = null,
        val subject: String,
        val body: String
) {
}