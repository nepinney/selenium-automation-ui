package acquire.recoup

data class MailingAddress(
        val company: String,
        val atnTo: String,
        val streetNumber: String,
        val streetName: String,
        val postalcode: String,
        val city: String,
        val phoneNumberArea: String,
        val phoneNumber: String,
        val suiteNumber: String? = null,
        val floorNumber: String? = null

) {
}