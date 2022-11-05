package dimensional.quic.tls.constants

public enum class TlsCipherSuite(public val code: Short) {
    // TLS 1.3 https://tools.ietf.org/html/rfc8446
    TLS_AES_128_GCM_SHA256       (0x1301),
    TLS_AES_256_GCM_SHA384       (0x1302),
    TLS_CHACHA20_POLY1305_SHA256 (0x1303),
    TLS_AES_128_CCM_SHA256       (0x1304),
    TLS_AES_128_CCM_8_SHA256     (0x1305),
    ;

    public companion object {
        public fun byCode(code: Short): TlsCipherSuite? = values().find { it.code == code }
    }
}
