package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsNamedGroup

public expect val AVAILABLE_NAMED_GROUPS: List<TlsNamedGroup>

internal expect fun createKeyPairGenerator(namedGroup: TlsNamedGroup): KeyPairGenerator

internal sealed interface Key {
    fun export(): ByteArray
}

internal interface PublicKey : Key

internal interface PrivateKey : Key

internal interface KeyPair {
    val public: PublicKey

    val private: PrivateKey
}

internal fun interface KeyPairGenerator {
    fun generate(): KeyPair
}
