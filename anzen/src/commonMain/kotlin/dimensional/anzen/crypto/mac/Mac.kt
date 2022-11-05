package dimensional.anzen.crypto.mac

import dimensional.anzen.crypto.params.CryptoParameters

public interface Mac {
    /** The name of the algorithm this MAC implements. */
    public val algorithmName: String

    /** The block size for this MAC (in bytes) */
    public val macSize: Int

    /** Initializes this MAC with the supplied parameters. */
    public fun initialize(params: CryptoParameters): Mac

    /** Updates this digest with a single byte. */
    public fun update(value: Byte): Mac

    /** Updates this Mac with a block of bytes. */
    public fun update(src: ByteArray, srcOff: Int = 0, srcLen: Int = src.size): Mac

    /** Finalizes this Mac. Stores the final value in [dst], resetting afterwards. */
    public fun finalize(dst: ByteArray, dstOff: Int = 0): Int

    /** Resets this Mac back to it's initial state. */
    public fun reset(): Mac
}
