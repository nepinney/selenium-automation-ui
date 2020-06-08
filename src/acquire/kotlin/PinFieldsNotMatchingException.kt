package acquire.kotlin

/**
 * @Author Nicholas Pinney
 */
class PinFieldsNotMatchingException internal constructor() : Exception() {
    private val error = "Pins Didn't Match"
    override fun toString(): String {
        return error
    }
}