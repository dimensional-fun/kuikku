package dimensional.quic.tls.crypto

import java.security.SecureRandom

private val random = SecureRandom()

internal actual fun fillRandomBytes(dst: ByteArray) {
    random.nextBytes(dst)
}
