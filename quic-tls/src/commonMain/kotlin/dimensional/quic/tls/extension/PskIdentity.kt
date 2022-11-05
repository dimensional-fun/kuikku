package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.writeOpaque16
import io.ktor.utils.io.core.*

public data class PskIdentity(
    val identity: ByteArray,
    val obfuscatedTicketAge: Long
) {
    public companion object : TlsMarshallingFactory<PskIdentity> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): PskIdentity = PskIdentity(
            payload.readOpaque16(),
            payload.readUInt().toLong()
        )

        override fun marshal(ctx: TlsContext, value: PskIdentity): ByteReadPacket = buildPacket {
            writeOpaque16(value.identity)
            writeUInt(value.obfuscatedTicketAge.toUInt())
        }
    }

    public val estimatedSize: Int = 2 + identity.size + 4
}
