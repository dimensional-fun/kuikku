package dimensional.anzen.crypto.mac

import java.security.NoSuchAlgorithmException

internal actual fun targetMac(name: String): Mac? = try {
    val delegate = javax.crypto.Mac.getInstance(name)
    JvmMac(delegate)
} catch (ex: NoSuchAlgorithmException) {
    null
}
