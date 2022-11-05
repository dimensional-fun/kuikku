package dimensional.quic.tls.extension

public enum class NameType(public val code: Int) {
    HostName(0),
    ;

    public companion object {
        public fun byCode(code: Int): NameType = values().first { it.code == code }
    }
}
