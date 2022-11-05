package dimensional.anzen.crypto.params

public data class KeyParameter(
    public val key: ByteArray,
    public val algorithm: String,
) : CryptoParameters
