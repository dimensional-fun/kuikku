package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsSignatureScheme

internal expect val AVAILABLE_SIGNATURES: List<TlsSignatureScheme>

internal expect fun createSigner(key: PrivateKey, scheme: TlsSignatureScheme): Signer

internal interface Signer {
    fun update(value: Byte): Signer

    fun update(src: ByteArray, srcOff: Int, srcLen: Int): Signer

    fun sign(): ByteArray
}

internal expect fun createVerifier(key: PublicKey, scheme: TlsSignatureScheme): Verifier

internal interface Verifier {
    fun update(value: Byte): Verifier

    fun update(src: ByteArray, srcOff: Int, srcLen: Int): Verifier

    fun verify(sig: ByteArray): Boolean
}
