package acquire.kotlin

import acquire.kotlin.recoup.RecoupActionTypes
import javafx.scene.control.Button

class EmailButton(private val name: String): Button(name) {

    fun buildEmail(actionType: RecoupActionTypes) {
        println("Click $actionType")
    }
/*    override fun onAction() {
        println("This Worked!!!")
    }*/

}