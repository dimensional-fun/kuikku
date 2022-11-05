package dimensional.anzen.crypto.mac

import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.exception.UnsupportedAlgorithmException

internal expect fun targetMac(name: String): Mac?

internal fun anzenMac(name: String, preferPlatformImplementation: Boolean): Mac? {
    if ("hmac" in name) {
        val digest = when {
            "md5"    in name -> Digest("MD5",    preferPlatformImplementation)
            "sha256" in name -> Digest("SHA256", preferPlatformImplementation)
            "sha384" in name -> Digest("SHA384", preferPlatformImplementation)
            "sha512" in name -> Digest("SHA512", preferPlatformImplementation)
            else -> return null
        }

        return HMAC(digest)
    }

    return null
}

/**
 * Returns a [Mac] instance for the given [algorithm name][name].
 *
 * @param name
 *   The MAC algorithm name
 *
 * @param preferPlatformImplementation
 *   Whether to prefer native MAC implementations over ours.
 */
public fun Mac(
    name: String,
    preferPlatformImplementation: Boolean = true
): Mac {
    val name = name.lowercase()

    /* find a MAC implementation */
    val mac = if (preferPlatformImplementation) {
        targetMac(name) ?: anzenMac(name, preferPlatformImplementation)
    } else {
        anzenMac(name, preferPlatformImplementation) ?: targetMac(name)
    }

    return mac ?: throw UnsupportedAlgorithmException(name)
}

public fun Mac.finalize(): ByteArray {
    val final = ByteArray(macSize)
    finalize(final, 0)

    return final
}

public operator fun Mac.plusAssign(value: Byte) {
    update(value)
}

public operator fun Mac.plusAssign(value: ByteArray) {
    update(value, 0, value.size)
}
