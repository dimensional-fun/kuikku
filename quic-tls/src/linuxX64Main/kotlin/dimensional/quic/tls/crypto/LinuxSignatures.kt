package dimensional.quic.tls.crypto

import dimensional.quic.tls.constants.TlsSignatureScheme

// TODO: add wolfSSL

internal actual val AVAILABLE_SIGNATURES: List<TlsSignatureScheme> = emptyList()

internal actual fun createVerifier(key: PublicKey, scheme: TlsSignatureScheme): Verifier {
    TODO("Not yet implemented")
}

internal actual fun createSigner(key: PrivateKey, scheme: TlsSignatureScheme): Signer {
    TODO("Not yet implemented")
}
