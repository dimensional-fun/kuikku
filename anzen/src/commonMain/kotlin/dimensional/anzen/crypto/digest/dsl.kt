package dimensional.anzen.crypto.digest

import dimensional.anzen.crypto.digest.md.MD5
import dimensional.anzen.crypto.digest.sha.SHA256
import dimensional.anzen.crypto.digest.sha.SHA384
import dimensional.anzen.crypto.digest.sha.SHA512
import dimensional.anzen.crypto.exception.UnsupportedAlgorithmException

internal expect fun targetDigest(name: String): Digest?

internal fun anzenDigest(name: String): Digest? = when (name) {
    "md5"    -> MD5()
    "sha256" -> SHA256()
    "sha384" -> SHA384()
    "sha512" -> SHA512()
    else -> null
}

/**
 * Returns a [Digest] instance for the given [algorithm name][name].
 *
 * @param name
 *   The message digest algorithm name
 *
 * @param preferPlatformImplementation
 *   Whether to prefer native message digest implementations over ours.
 */
public fun Digest(
    name: String,
    preferPlatformImplementation: Boolean = true
): Digest {
    val name = name.lowercase()

    /* find message digest implementation */
    val digest = if (preferPlatformImplementation) {
        targetDigest(name) ?: anzenDigest(name)
    } else {
        anzenDigest(name) ?: targetDigest(name)
    }

    return digest ?: throw UnsupportedAlgorithmException(name)
}

public fun Digest.digest(input: ByteArray, offset: Int = 0, length: Int = input.size): ByteArray {
    update(input, offset, length)
    return finalize()
}

public fun Digest.finalize(): ByteArray {
    val final = ByteArray(digestSize)
    finalize(final, 0)

    return final
}

public fun ByteArray.digest(name: String, offset: Int = 0, length: Int = size): ByteArray = Digest(name)
    .update(this, offset, length)
    .finalize()


public operator fun Digest.plusAssign(value: Byte) {
    update(value)
}

public operator fun Digest.plusAssign(value: ByteArray) {
    update(value, 0, value.size)
}
