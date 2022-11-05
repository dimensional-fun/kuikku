package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsNamedGroup

public actual val AVAILABLE_NAMED_GROUPS: List<TlsNamedGroup> = emptyList()

internal actual fun createKeyPairGenerator(namedGroup: TlsNamedGroup): KeyPairGenerator {
    error("Unsupported named group: $namedGroup")
}
