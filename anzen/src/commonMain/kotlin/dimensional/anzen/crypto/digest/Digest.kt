package dimensional.anzen.crypto.digest

public interface Digest {
    /** The name of the algorithm being used. */
    public val algorithmName: String

    /** The size of the digest produced. */
    public val digestSize: Int

    /** Updates this digest with a single byte. */
    public fun update(value: Byte): Digest

    /** Updates this digest with a block of bytes. */
    public fun update(src: ByteArray, srcOff: Int = 0, srcLen: Int = src.size): Digest

    /** Finalizes this digest. Stores the final value in [dst], resetting afterwards. */
    public fun finalize(dst: ByteArray, dstOff: Int = 0): Int

    /** Resets this digest back to it's initial state. */
    public fun reset(): Digest
}
