package acquire.recoup

data class WaybillInformation(
        val company: String,
        val atnTo: String,
        val streetNumber: String,
        val streetName: String,
        val postalcode: String,
        val city: String,
        val phoneNumberArea: String,
        val phoneNumber: String,
        val suiteNumber: String? = null,
        val floorNumber: String? = null,
        var outboundPin: String? = null,
        var inboundPin: String? = null

) {
}