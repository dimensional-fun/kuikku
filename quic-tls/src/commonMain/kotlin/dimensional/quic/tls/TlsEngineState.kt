package dimensional.quic.tls

import dimensional.quic.tls.crypto.CryptoState
import dimensional.quic.tls.crypto.KeyPair
import dimensional.quic.tls.crypto.PublicKey

public class TlsEngineState {
    /** Our local key-pair used to creat ethe */
    internal lateinit var localKeyPair: KeyPair

    /** The public key the peer shared with us. */
    internal lateinit var peerPublicKey: PublicKey

    /** The cyrpto state */
    internal val crypto = CryptoState(this)
}
