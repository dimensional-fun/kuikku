package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.readOpaque16List
import dimensional.quic.tls.tools.writeOpaque16List
import io.ktor.utils.io.core.*

public sealed class KeyShareExtension : TlsExtension(TlsExtensionType.KeyShare) {
    public companion object : TlsExtensionFactory<KeyShareExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): KeyShareExtension = when (handshake) {
            TlsHandshakeType.ClientHello -> ClientHello.unmarshal(ctx, handshake, payload)
            TlsHandshakeType.ServerHello -> ServerHello.unmarshal(ctx, handshake, payload)
            else -> error("The `key_share` extension does not support `$handshake` handshakes.")
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: KeyShareExtension
        ): ByteReadPacket = when (handshake) {
            TlsHandshakeType.ClientHello -> ClientHello.marshal(ctx, handshake, extension.clientHello!!)
            TlsHandshakeType.ServerHello -> ServerHello.marshal(ctx, handshake, extension.serverHello!!)
            else -> error("The `key_share` extension does not support `$handshake` handshakes.")
        }
    }

    public val clientHello: ClientHello?
        get() = this as? ClientHello

    public val serverHello: ServerHello?
        get() = this as? ServerHello

    public data class ServerHello(public val selectedGroup: TlsNamedGroup) : KeyShareExtension() {
        public companion object : TlsExtensionFactory<ServerHello> {
            override fun unmarshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                payload: ByteReadPacket,
            ): ServerHello {
                require (handshake == TlsHandshakeType.ServerHello) {
                    "Expected handshake type `ServerHello`, got `$handshake`"
                }

                val selectedGroup = TlsNamedGroup.byCode(payload.readShort())
                return ServerHello(selectedGroup)
            }

            override fun marshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                extension: ServerHello,
            ): ByteReadPacket = buildPacket { writeShort(extension.selectedGroup.code) }
        }

        override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
            marshal(ctx, handshake, this)

        override fun toString(): String = "KeyShareExtension::ServerHello(selectedGroup=$selectedGroup)"
    }

    public data class ClientHello(public val entries: List<KeyShareEntry>) : KeyShareExtension() {
        public companion object : TlsExtensionFactory<ClientHello> {
            override fun unmarshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                payload: ByteReadPacket,
            ): ClientHello {
                val entries = payload.readOpaque16List { opaque ->
                    KeyShareEntry(ctx, opaque).some()
                }

                return ClientHello(entries)
            }

            override fun marshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                extension: ClientHello,
            ): ByteReadPacket = buildPacket {
                writeOpaque16List(extension.entries) { opaque, kse ->
                    val packet = KeyShareEntry.marshal(ctx, kse)
                    opaque.writePacket(packet)
                }
            }
        }

        override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
            marshal(ctx, handshake, this)

        override fun toString(): String = "KeyShareExtension::ClientHello(entries=$entries)"
    }
}
