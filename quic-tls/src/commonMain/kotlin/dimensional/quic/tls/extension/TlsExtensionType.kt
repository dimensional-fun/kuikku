package dimensional.quic.tls.extension

// See https://www.rfc-editor.org/rfc/rfc8446.html#section-4.1.2
public enum class TlsExtensionType(public val code: Short) {
    Unknown (-1),

    ServerName                          (0),
    MaxFragmentLength                   (1),
    StatusRequest                       (5),
    SupportedGroups                     (10),
    SignatureAlgorithms                 (13),
    UseSRTP                             (14),
    Heartbeat                           (15),
    ApplicationLayerProtocolNegotiation (16),
    SignedCertificateTimeout            (18),
    ClientCertificateType               (19),
    ServerCertificateType               (20),
    Padding                             (21),
    CompressCertificate                 (27),
    PreSharedKey                        (41),
    EarlyData                           (42),
    SupportedVersions                   (43),
    Cookie                              (44),
    PskKeyExchangeModes                 (45),
    CertificateAuthorities              (47),
    OIDFilters                          (48),
    PostHandshakeAuth                   (49),
    SignatureAlgorithmsCert             (50),
    KeyShare                            (51),

    ApplicationSettings                 (17513),
    ;


    public companion object {
        public operator fun get(code: Short): TlsExtensionType = values().find { it.code == code }
            ?: Unknown
    }
}
