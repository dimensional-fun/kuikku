package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.writeOpaque16
import io.ktor.utils.io.core.*

public data class KeyShareEntry(val group: TlsNamedGroup, val key: ByteArray) {
    public companion object : TlsMarshallingFactory<KeyShareEntry> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): KeyShareEntry = KeyShareEntry(
            TlsNamedGroup.byCode(payload.readShort()),
            payload.readOpaque16()
        )

        override fun marshal(ctx: TlsContext, value: KeyShareEntry): ByteReadPacket = buildPacket {
            writeShort(value.group.code)
            writeOpaque16(value.key)
        }
    }

    /** The estimated size of this key_share_entry. */
    val estimatedSize: Int = 2 + 2 + key.size // group + key_size + key
}
