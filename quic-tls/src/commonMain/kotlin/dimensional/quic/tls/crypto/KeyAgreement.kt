package dimensional.quic.tls.crypto

internal expect fun performKeyAgreement(
    private: PrivateKey,
    public:  PublicKey,
): ByteArray
