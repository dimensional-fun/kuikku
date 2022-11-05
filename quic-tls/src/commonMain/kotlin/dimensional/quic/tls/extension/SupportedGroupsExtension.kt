package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.writeOpaque16List
import io.ktor.utils.io.core.*

public data class SupportedGroupsExtension(
    val groups: List<TlsNamedGroup>
) : TlsExtension(TlsExtensionType.SupportedGroups) {
    public companion object : TlsExtensionFactory<SupportedGroupsExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): SupportedGroupsExtension {
            val groups = List(payload.readShort() / 2) {
                val code = payload.readShort()
                TlsNamedGroup.byCode(code)
            }

            return SupportedGroupsExtension(groups)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: SupportedGroupsExtension
        ): ByteReadPacket = buildPacket {
            writeOpaque16List(extension.groups) { opaque, group -> opaque.writeShort(group.code) }
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
