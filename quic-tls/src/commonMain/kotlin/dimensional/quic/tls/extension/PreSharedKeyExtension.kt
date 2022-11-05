package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.readOpaque16List
import dimensional.quic.tls.tools.writeOpaque16List
import io.ktor.utils.io.core.*

public sealed class PreSharedKeyExtension : TlsExtension(TlsExtensionType.PreSharedKey) {
    public companion object : TlsExtensionFactory<PreSharedKeyExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): PreSharedKeyExtension = when (handshake) {
            TlsHandshakeType.ClientHello -> ClientHello.unmarshal(ctx, handshake, payload)
            TlsHandshakeType.ServerHello -> ServerHello.unmarshal(ctx, handshake, payload)
            else -> error("The `pre_shared_key` extension does not support `$handshake` handshakes.")
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: PreSharedKeyExtension
        ): ByteReadPacket = when (handshake) {
            TlsHandshakeType.ClientHello -> ClientHello.marshal(ctx, handshake, extension.clientHello!!)
            TlsHandshakeType.ServerHello -> ServerHello.marshal(ctx, handshake, extension.serverHello!!)
            else -> error("The `pre_shared_key` extension does not support `$handshake` handshakes.")
        }
    }

    public val clientHello: ClientHello?
        get() = this as? ClientHello

    public val serverHello: ServerHello?
        get() = this as? ServerHello

    public data class ClientHello(
        val identities: List<PskIdentity>,
        val binders: List<PskBinderEntry>,
    ) : PreSharedKeyExtension() {
        public companion object : TlsExtensionFactory<ClientHello> {
            override fun unmarshal(ctx: TlsContext, handshake: TlsHandshakeType, payload: ByteReadPacket): ClientHello {
                require (handshake == TlsHandshakeType.ServerHello) {
                    "Expected handshake type to be ClientHello, not $handshake"
                }

                return ClientHello(
                    payload.readIdentities(ctx),
                    payload.readBinders(ctx)
                )
            }

            override fun marshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                extension: ClientHello,
            ): ByteReadPacket = buildPacket {
                require (handshake == TlsHandshakeType.ServerHello) {
                    "Expected handshake type to be ServerHello, not $handshake"
                }

                writeOpaque16List(extension.identities) { opaque, identity ->
                    val packet = PskIdentity.marshal(ctx, identity)
                    opaque.writePacket(packet)
                }

                writeOpaque16List(extension.binders) { opaque, binder ->
                    val packet = PskBinderEntry.marshal(ctx, binder)
                    opaque.writePacket(packet)
                }
            }

            private fun ByteReadPacket.readIdentities(ctx: TlsContext): List<PskIdentity> {
                return readOpaque16List { opaque ->
                    val identity = PskIdentity(ctx, opaque)
                    identity.some()
                }

//                val identities = mutableListOf<PskIdentity>()
//
//                var remaining = readShort().toInt()
//                while (remaining > 0) {
//                    val identity = PskIdentity.unmarshal(ctx, this)
//                    identities += identity
//                    remaining  -= identity.estimatedSize
//                }
//
//                return identities
            }

            private fun ByteReadPacket.readBinders(ctx: TlsContext): List<PskBinderEntry> {
                return readOpaque16List { opaque ->
                    val binder = PskBinderEntry(ctx, opaque)
                    binder.some()
                }

//                val binders = mutableListOf<PskBinderEntry>()
//
//                var remaining = readShort().toInt()
//                while (remaining > 0) {
//                    val binder = PskBinderEntry.unmarshal(ctx, this)
//                    binders   += binder
//                    remaining -= binder.estimatedSize
//                }
//
//                return binders
            }
        }

        override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
            marshal(ctx, handshake, this)

        override fun toString(): String = "PreSharedKeyExtension::ClientHello(identities=$identities, binders=$binders)"
    }

    public data class ServerHello(val selectedIdentity: Short): PreSharedKeyExtension() {
        public companion object : TlsExtensionFactory<ServerHello> {
            override fun unmarshal(ctx: TlsContext, handshake: TlsHandshakeType, payload: ByteReadPacket): ServerHello {
                val selectedIdentity = payload.readShort()
                return ServerHello(selectedIdentity)
            }

            override fun marshal(
                ctx: TlsContext,
                handshake: TlsHandshakeType,
                extension: ServerHello,
            ): ByteReadPacket = buildPacket {
                writeShort(extension.selectedIdentity)
            }
        }

        override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
            marshal(ctx, handshake, this)

        override fun toString(): String = "PreSharedKeyExtension::ServerHello(selectedIdentity=$selectedIdentity)"
    }
}
