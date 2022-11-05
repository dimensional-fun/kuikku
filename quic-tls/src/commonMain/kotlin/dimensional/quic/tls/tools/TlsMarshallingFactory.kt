package dimensional.quic.tls.tools

import dimensional.quic.tls.TlsContext
import io.ktor.utils.io.core.*

public interface TlsMarshallingFactory<R> {
    public fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): R

    public fun marshal  (ctx: TlsContext, value: R): ByteReadPacket

    public operator fun invoke(ctx: TlsContext, payload: ByteReadPacket): R {
        return unmarshal(ctx, payload)
    }
}
