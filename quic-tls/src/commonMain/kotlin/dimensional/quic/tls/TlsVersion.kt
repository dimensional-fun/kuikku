package dimensional.quic.tls

public enum class TlsVersion(public val code: Short) {
    Unknown(-1),

    Reserved(0xcaca.toShort()),

    TLS10(0x0301),
    TLS11(0x0302),
    TLS12(0x0303),
    TLS13(0x0304),
    ;


    public companion object {
        public fun byCode(code: Short): TlsVersion = values().find { it.code == code } ?: Unknown
    }
}
