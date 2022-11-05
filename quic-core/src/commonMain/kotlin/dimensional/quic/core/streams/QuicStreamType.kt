package dimensional.quic.core.streams

public enum class QuicStreamType {
    Bidirectional,
    Unidirectional,
    ;

    public fun isUni(): Boolean  = this == Unidirectional

    public fun isBidi(): Boolean = !isUni()
}
