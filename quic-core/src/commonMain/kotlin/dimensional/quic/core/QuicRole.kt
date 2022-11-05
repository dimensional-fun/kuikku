package dimensional.quic.core

public enum class QuicRole {
    Server,
    Client,
    ;

    public fun other(): QuicRole = if (this == Server) Client else Server
}
