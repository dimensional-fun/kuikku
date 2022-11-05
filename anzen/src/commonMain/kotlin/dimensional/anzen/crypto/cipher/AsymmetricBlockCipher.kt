package dimensional.anzen.crypto.cipher

import dimensional.anzen.crypto.params.CryptoParameters

public interface AsymmetricBlockCipher {
    /** The largest block size this cipher can receive. */
    public val inputBlockSize: Int

    /** The largest block size this cipher can produce. */
    public val outputBlockSize: Int

    /** Initialize this block cipher. */
    public fun init(encrypting: Boolean, params: CryptoParameters)

    /** Processes the supplied block. */
    public fun processBlock(block: ByteArray, offset: Int = 0, length: Int = block.size)
}
