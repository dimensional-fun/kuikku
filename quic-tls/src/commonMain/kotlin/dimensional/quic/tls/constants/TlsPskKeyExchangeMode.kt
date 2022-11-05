package dimensional.quic.tls.constants

public enum class TlsPskKeyExchangeMode(public val code: Byte) {
    psk_ke(0),
    psk_dhe_ke(1),
    ;

    public companion object {
        public operator fun get(code: Byte): TlsPskKeyExchangeMode = values().first { it.code == code }
    }
}
