package dimensional.anzen.crypto.digest

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

internal actual fun targetDigest(name: String): Digest? = try {
    val digest = MessageDigest.getInstance(name)
    JvmDigest(digest)
} catch (ex: NoSuchAlgorithmException) {
    null
}
