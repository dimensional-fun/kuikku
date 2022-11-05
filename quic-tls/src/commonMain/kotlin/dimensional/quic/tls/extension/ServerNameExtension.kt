package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.readOpaque16List
import dimensional.quic.tls.tools.writeOpaque16List
import io.ktor.utils.io.core.*

public data class ServerNameExtension(
    val serverNames: List<ServerName>
) : TlsExtension(TlsExtensionType.ServerName) {
    public companion object : TlsExtensionFactory<ServerNameExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): ServerNameExtension {
            val serverNames = payload.readOpaque16List { opaque ->
                ServerName(ctx, opaque).some()
            }

            return ServerNameExtension(serverNames)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: ServerNameExtension
        ): ByteReadPacket = buildPacket {
            writeOpaque16List(extension.serverNames) { opaque, serverName ->
                val packet = ServerName.marshal(ctx, serverName)
                opaque.writePacket(packet)
            }
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
