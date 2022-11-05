package dimensional.anzen.crypto.exception

public class UnsupportedAlgorithmException(
    public val algorithm: String
) : IllegalStateException("The '$algorithm' algorithm is not supported")
