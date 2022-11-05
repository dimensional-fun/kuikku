package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*

public data class EarlyDataExtension(
    public val maxEarlyDataSize: Long?
) : TlsExtension(TlsExtensionType.EarlyData) {
    public companion object : TlsExtensionFactory<EarlyDataExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): EarlyDataExtension {
            val maxEarlyDataSize = if (payload.remaining == 4L) {
                payload.readInt().toLong()
            } else {
                null
            }

            return EarlyDataExtension(maxEarlyDataSize)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: EarlyDataExtension
        ): ByteReadPacket = buildPacket {
            extension.maxEarlyDataSize
                ?.toInt()
                ?.let { writeInt(it) }
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
