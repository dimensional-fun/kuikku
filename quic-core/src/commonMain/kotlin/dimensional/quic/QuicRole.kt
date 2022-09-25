package dimensional.quic

public enum class QuicRole {
    Server,
    Client,
    ;

    public fun other(): dimensional.quic.QuicRole = if (this == dimensional.quic.QuicRole.Server) dimensional.quic.QuicRole.Client else dimensional.quic.QuicRole.Server
}
