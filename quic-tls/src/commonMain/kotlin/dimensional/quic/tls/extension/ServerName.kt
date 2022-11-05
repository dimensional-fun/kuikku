package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.writeOpaque16
import io.ktor.utils.io.core.*

public data class ServerName(val name: String, val type: NameType = NameType.HostName) {
    public companion object : TlsMarshallingFactory<ServerName> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): ServerName {
            val type = NameType.byCode(payload.readByte().toInt())
            return ServerName(
                payload.readOpaque16().decodeToString(),
                type
            )
        }

        override fun marshal(ctx: TlsContext, value: ServerName): ByteReadPacket = buildPacket {
            writeByte(value.type.code.toByte())
            writeOpaque16(value.name.encodeToByteArray())
        }
    }

    val estimatedSize: Int = 1 + 2 + name.length // name_type + name_len + name
}
