package dimensional.quic.tls.crypto

import dimensional.quic.tls.TlsEngineState

internal expect class CryptoState constructor(state: TlsEngineState) {
    /** Create handshake secrets. */
    actual fun createHandshakeSecrets()

    /** Create application secrets */
    actual fun createApplicationSecrets()
}
