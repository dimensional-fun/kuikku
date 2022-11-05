package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.*
import io.ktor.utils.io.core.*

public data class ApplicationLayerProtocolNegotiationExtension(
    val protocols: List<String>
) : TlsExtension(TlsExtensionType.ApplicationLayerProtocolNegotiation) {
    public companion object : TlsExtensionFactory<ApplicationLayerProtocolNegotiationExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): ApplicationLayerProtocolNegotiationExtension {
            val protocols = payload.readOpaque16List { opaque ->
                opaque.readOpaque8()
                    .decodeToString()
                    .some()
            }

            return ApplicationLayerProtocolNegotiationExtension(protocols)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: ApplicationLayerProtocolNegotiationExtension
        ): ByteReadPacket = buildPacket {
            writeOpaque16List(extension.protocols) { opaque, protocol ->
                val protocolBytes = protocol.encodeToByteArray()
                opaque.writeOpaque8(protocolBytes)
            }
         }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
