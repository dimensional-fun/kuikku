package dimensional.quic.tls.crypto

import java.security.interfaces.ECPublicKey
import java.security.interfaces.XECPublicKey
import javax.crypto.KeyAgreement

internal actual fun performKeyAgreement(private: PrivateKey, public: PublicKey): ByteArray {
    val keyagr = when (public.java) {
        is ECPublicKey  -> KeyAgreement.getInstance("ECDH")
        is XECPublicKey -> KeyAgreement.getInstance("XDH")
        else            -> error("Unsupported")
    }

    keyagr.init(private.java)
    keyagr.doPhase(public.java, true)

    return keyagr.generateSecret()
}
