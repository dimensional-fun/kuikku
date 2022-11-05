package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.constants.TlsNamedGroup.*
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.ECGenParameterSpec
import java.security.spec.NamedParameterSpec
import java.security.KeyPairGenerator as JavaSecurityKeyPairGenerator

public actual val AVAILABLE_NAMED_GROUPS: List<TlsNamedGroup> = listOf(
    secp256r1, secp384r1, secp521r1,

    x448, x25519
)

internal actual fun createKeyPairGenerator(namedGroup: TlsNamedGroup): KeyPairGenerator {
    val params: AlgorithmParameterSpec
    val delegate = when (namedGroup) {
        secp256r1, secp384r1, secp521r1 -> {
            params = ECGenParameterSpec(namedGroup.name)
            JavaSecurityKeyPairGenerator.getInstance("EC")
        }

        x448, x25519 -> {
            params = NamedParameterSpec(namedGroup.name.uppercase())
            JavaSecurityKeyPairGenerator.getInstance("XDH")
        }

        else -> error("Unsupported Named Group: $namedGroup")
    }

    delegate.initialize(params)
    return JavaKeyPairGenerator(delegate)
}

internal val PrivateKey.java: java.security.PrivateKey get() = (this as JavaPrivateKey).delegate

internal val PublicKey.java: java.security.PublicKey get() = (this as JavaPublicKey).delegate

@JvmInline
internal value class JavaPublicKey(val delegate: java.security.PublicKey) : PublicKey {
    override fun export(): ByteArray = delegate.encoded
}

@JvmInline
internal value class JavaPrivateKey(val delegate: java.security.PrivateKey) : PrivateKey {
    override fun export(): ByteArray = delegate.encoded
}

@JvmInline
internal value class JavaKeyPair(val delegate: java.security.KeyPair) : KeyPair {
    override val private: PrivateKey get() = JavaPrivateKey(delegate.private)

    override val public:  PublicKey  get() = JavaPublicKey(delegate.public)
}

@JvmInline
internal value class JavaKeyPairGenerator(val bc: JavaSecurityKeyPairGenerator): KeyPairGenerator {
    override fun generate(): KeyPair = JavaKeyPair(bc.generateKeyPair())
}
