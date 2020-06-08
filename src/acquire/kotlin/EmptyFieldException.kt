package acquire.kotlin

/**
 * @Author Nicholas Pinney
 */
class EmptyFieldException(private val emptyField: String) : Exception() {

    override fun toString(): String {
        return emptyField
    }

}