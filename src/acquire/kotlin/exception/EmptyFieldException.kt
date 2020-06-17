package acquire.kotlin.exception

/**
 * @Author Nicholas Pinney
 */
class EmptyFieldException(private val emptyField: String) : Exception() {

    override fun toString(): String {
        return emptyField
    }

}