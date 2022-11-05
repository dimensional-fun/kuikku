package dimensional.quic.tls.handshake

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.tools.TlsMarshallingFactory
import io.ktor.utils.io.core.*

public data class TlsFinishedHandshake(public val verifiedData: ByteArray) : TlsHandshake(TlsHandshakeType.Finished) {
    public companion object : TlsMarshallingFactory<TlsFinishedHandshake> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): TlsFinishedHandshake {
            return TlsFinishedHandshake(payload.readBytes())
        }

        override fun marshal(ctx: TlsContext, value: TlsFinishedHandshake): ByteReadPacket =
            ByteReadPacket(value.verifiedData)
    }

    override fun marshalPayload(ctx: TlsContext): ByteReadPacket = marshal(ctx, this)
}
