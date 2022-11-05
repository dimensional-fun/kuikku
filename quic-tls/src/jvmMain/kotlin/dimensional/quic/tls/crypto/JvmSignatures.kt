package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsSignatureScheme
import dimensional.quic.tls.constants.TlsSignatureScheme.*
import java.security.Signature
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PSSParameterSpec

public actual val AVAILABLE_SIGNATURES: List<TlsSignatureScheme> = listOf(
    rsa_pkcs1_sha256,
    rsa_pkcs1_sha384,
    rsa_pkcs1_sha512,

    ecdsa_secp256r1_sha256,
    ecdsa_secp384r1_sha384,
    ecdsa_secp521r1_sha512,

    rsa_pss_rsae_sha256,
    rsa_pss_rsae_sha384,
    rsa_pss_rsae_sha512,

    ed25519,
    ed448,

//    rsa_pss_pss_sha256,
//    rsa_pss_pss_sha384,
//    rsa_pss_pss_sha512,
)

internal actual fun createVerifier(key: PublicKey, scheme: TlsSignatureScheme): Verifier {
    val delegate = createJavaSignature(scheme)
    delegate.initVerify(key.java)

    return JavaSignature(delegate)
}

internal actual fun createSigner(key: PrivateKey, scheme: TlsSignatureScheme): Signer {
    val delegate = createJavaSignature(scheme)
    delegate.initSign(key.java)

    return JavaSignature(delegate)
}

private fun createJavaSignature(scheme: TlsSignatureScheme): Signature {
    var params: AlgorithmParameterSpec? = null

    val delegate = when (scheme) {
        /* RSASSA-PKCS1-v1_5 algorithms */
        rsa_pkcs1_sha256 ->
            Signature.getInstance("SHA256withRSA")
        rsa_pkcs1_sha384 ->
            Signature.getInstance("SHA384withRSA")
        rsa_pkcs1_sha512 ->
            Signature.getInstance("SHA512withRSA")

        /* ECDSA algorithms */
        ecdsa_secp256r1_sha256 ->
            Signature.getInstance("SHA256withECDSA")
        ecdsa_secp384r1_sha384 ->
            Signature.getInstance("SHA384withECDSA")
        ecdsa_secp521r1_sha512 ->
            Signature.getInstance("SHA512withECDSA")

        /* RSASSA-PSS algorithms with public key OID rsaEncryption */
        rsa_pss_rsae_sha256 -> {
            params = PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec("SHA-256"), 32, 1)
            Signature.getInstance("RSASSA-PSS")
        }
        rsa_pss_rsae_sha384 -> {
            params = PSSParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec("SHA-384"), 48, 1)
            Signature.getInstance("RSASSA-PSS")
        }
        rsa_pss_rsae_sha512 -> {
            params = PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec("SHA-512"), 64, 1)
            Signature.getInstance("RSASSA-PSS")
        }

        /* EdDSA algorithms */
        ed25519 ->
            Signature.getInstance("Ed25519")
        ed448 ->
            Signature.getInstance("Ed448")

        else -> error("Unsupported signature scheme: $scheme")
    }

    params?.let { delegate.setParameter(params) }
    return delegate
}

@JvmInline
internal value class JavaSignature(val delegate: Signature) : Signer, Verifier {
    override fun update(value: Byte): JavaSignature {
        delegate.update(value)
        return this
    }

    override fun update(src: ByteArray, srcOff: Int, srcLen: Int): JavaSignature {
        delegate.update(src, srcOff, srcLen)
        return this
    }

    override fun sign(): ByteArray = delegate.sign()

    override fun verify(sig: ByteArray): Boolean = delegate.verify(sig)
}
