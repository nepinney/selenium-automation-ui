package acquire.login

import acquire.login.AES.decrypt
import acquire.login.AES.encrypt
import acquire.Config
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Scanner

/**
 * @Author Nicholas Pinney
 */
class CredentialManager(private val pin: String, private val username: String, private val password: String) {

    //private val credPath = "src\\resources\\Cred.txt"

    /**
     * Encrypts the credentials and writes them out to a file
     * @return Boolean for success or fail of file creation
     */
    fun createCredentialFile(): Boolean {
        val encryptedCredentials = listOf(
            encrypt(pin, pin + secrets[0]),
            encrypt(username, pin + secrets[1]),
            encrypt(password, pin + secrets[2])
        )
        return try {
            Files.write(
                Paths.get(Config.readPathToCredFile()),
                "Pin: ${encryptedCredentials[0]}\nUsername: ${encryptedCredentials[1]}\nPassword: ${encryptedCredentials[2]}".toByteArray()
            )
            true
        } catch (e: IOException) {
            println("Caught error creating Cred.txt: $e")
            false
        }
    }

    companion object {
        private val secrets = listOf("22214570002121966", "45685699544742400", "12549856265450026")
        //private const val credPath = "src/acquire/resources/Cred.txt"

        /**
         * Gets the encrypted pin from the file and sets it to encryptedPin
         */
        private val encryptedPin = fun(scan: Scanner): String {
            scan.useDelimiter(": ")
            scan.next()
            val temp = scan.next()
            return temp.substring(0, temp.indexOf('\n'))
        }

        private val encryptedUsername = fun(scan: Scanner): String {
            val temp = scan.next()
            return temp.substring(0, temp.indexOf('\n'))
        }

        private val encryptedPassword = fun(scan: Scanner): String {
            return scan.next()
        }

        /**
         * TODO: Use this same approach for the notesExtractor - scan passing to variables
         */
        fun decryptCredentialFile(enteredPin: String): List<String>{
            val credFile = File(Config.readPathToCredFile())
            val scan = Scanner(credFile)
            val encryptedCredentials = listOf(
                    encryptedPin(
                            scan
                    ),
                    encryptedUsername(scan),
                    encryptedPassword(scan)
            )
            scan.close()
            val encryptWithSecret = encryptedCredentials.zip(secrets)
            encryptWithSecret.forEach { println(it.first) }

            /*println("Decrypted: ")
            decryptedCredentials.forEach { println(it) }*/
            return encryptWithSecret.map { decrypt(it.first, enteredPin + it.second) }
        }
    }
}