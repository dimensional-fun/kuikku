package dimensional.quic.tls.crypto

import dimensional.quic.tls.TlsEngineState

internal actual class CryptoState actual constructor(
    val state: TlsEngineState
) {
    /** Create handshake secrets. */
    actual fun createHandshakeSecrets() {
    }

    /** Create application secrets */
    actual fun createApplicationSecrets() {
    }
}
