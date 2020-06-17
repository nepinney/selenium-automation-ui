package acquire.kotlin

import javafx.application.Application

/**
 * @Author Nicholas Pinney
 */
class Main {}

fun main(args: Array<String>) {
    println("Running from: ${Config.workingDirectory}")
    System.setProperty("javafx.preloader", acquire.kotlin.SplashPreLoader::class.java.canonicalName) //TODO: 4
    Application.launch(MyApplication::class.java)
}