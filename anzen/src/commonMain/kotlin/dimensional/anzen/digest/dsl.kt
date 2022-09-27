package dimensional.anzen.digest

public fun Digest.finalize(): ByteArray {
    val final = ByteArray(digestSize)
    finalize(final, 0)

    return final
}
