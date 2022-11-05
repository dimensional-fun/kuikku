package dimensional.anzen.crypto.signature

import dimensional.anzen.crypto.params.CryptoParameters

public interface Signature {
    /** Initialize this signer for signing or verification. */
    public fun initialize(signing: Boolean, parameters: CryptoParameters): Signature

    /** Update the internal digest with a single byte. */
    public fun update(value: Byte): Signature

    /** Update the internal digest with the provided byte array. */
    public fun update(src: ByteArray, offset: Int = 0, length: Int = src.size): Signature

    /** Generate a signature.*/
    public fun generate(): ByteArray

    /** TODO */
    public fun verify(sig: ByteArray): Boolean

    /** Resets this signature instance. */
    public fun reset(): Signature
}
