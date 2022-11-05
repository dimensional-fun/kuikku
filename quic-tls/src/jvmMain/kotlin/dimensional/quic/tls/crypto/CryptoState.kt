package dimensional.quic.tls.crypto

import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.digest.digest
import dimensional.quic.tls.TlsEngineState
import dimensional.quic.tls.tools.EmptyByteArray
import java.nio.ByteBuffer

internal actual class CryptoState actual constructor(
    val state: TlsEngineState,
) {
    companion object {
        private const val KEY_LENGTH      = 32 // Assuming AES-256, use 16 for AES-128

        private const val HASH_LENGTH     = 48 // Assuming SHA-384, use 32 for SHA-256

        private const val IV_LENGTH       = 12

        private const val AUTH_TAG_LENGTH = 16
    }

    private val hash = Digest("SHA${HASH_LENGTH * 8}")
    private val hkdf = HKDF(hash.algorithmName)

    private val emptyHash = hash.digest(EmptyByteArray)

    lateinit var earlySecret: ByteArray

    lateinit var binderKey: ByteArray

    lateinit var peerInfo: CipherInfo

    lateinit var localInfo: CipherInfo

    /** Create handshake secrets. */
    actual fun createHandshakeSecrets() {
//        val derivedSecret = hkdf.expandLabel(earlySecret)
    }

    /** Create application secrets */
    actual fun createApplicationSecrets() {
    }

    private fun createEarlySecret(ikm: ByteArray) {
        val salt = ByteArray(HASH_LENGTH)
        earlySecret = hkdf.extract(salt, ikm)
        binderKey   = hkdf.expandLabel(earlySecret, "res binder", emptyHash, HASH_LENGTH.toShort())
    }

    private fun createPskBinder() {
    }

    data class CipherInfo(val key: ByteArray, val iv: ByteArray)
}
