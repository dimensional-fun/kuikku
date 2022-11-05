package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*

public interface TlsExtensionFactory<E> {
    public fun unmarshal(ctx: TlsContext, handshake: TlsHandshakeType, payload: ByteReadPacket): E

    public fun marshal  (ctx: TlsContext, handshake: TlsHandshakeType, extension: E): ByteReadPacket
}
