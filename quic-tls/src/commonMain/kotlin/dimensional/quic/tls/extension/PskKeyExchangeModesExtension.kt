package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.constants.TlsPskKeyExchangeMode
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*

public data class PskKeyExchangeModesExtension(
    val modes: List<TlsPskKeyExchangeMode>
) : TlsExtension(TlsExtensionType.PskKeyExchangeModes) {
    public companion object : TlsExtensionFactory<PskKeyExchangeModesExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): PskKeyExchangeModesExtension {
            val modes = List(payload.readByte().toInt()) {
                val code = payload.readByte()
                TlsPskKeyExchangeMode[code]
            }

            return PskKeyExchangeModesExtension(modes)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: PskKeyExchangeModesExtension
        ): ByteReadPacket = buildPacket {
            writeByte(extension.modes.size.toByte())
            for (mode in extension.modes) writeByte(mode.code)
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
