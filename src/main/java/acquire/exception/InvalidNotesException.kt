package acquire.exception

import java.lang.Exception

/**
 * @Author Nicholas Pinney
 */
class InvalidNotesException internal constructor() : Exception() {
    private val error = "The notes retrieved from seleniumMethods were null or blank."
    override fun toString(): String {
        return error
    }
}