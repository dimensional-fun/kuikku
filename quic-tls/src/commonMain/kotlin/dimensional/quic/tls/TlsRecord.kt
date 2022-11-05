package dimensional.quic.tls

import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.writeOpaque16
import io.ktor.utils.io.core.*

/**  */
public data class TlsRecord(
    public val type: TlsRecordType,
    public val version: TlsVersion,
    public val payload: ByteReadPacket
) {
    public companion object : TlsMarshallingFactory<TlsRecord> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): TlsRecord {
            val type    = payload.readTLSRecordType()
                ?: error("Invalid TLS record type.")

            return TlsRecord(
                type,
                payload.readTLSVersion(),
                ByteReadPacket(payload.readOpaque16())
            )
        }

        /** Marshals this TLS record into a byte packet. */
        override fun marshal(ctx: TlsContext, value: TlsRecord): ByteReadPacket = buildPacket {
            writeByte(value.type.code.toByte())
            writeShort(value.version.code)
            writeOpaque16(value.payload)
        }


        public fun ByteReadPacket.readTLSRecordType(): TlsRecordType? {
            val code = readByte().toInt()
            return TlsRecordType.values().find { it.code == code }
        }

        public fun ByteReadPacket.readTLSVersion(): TlsVersion {
            val code = readShort()
            return TlsVersion.byCode(code)
        }
    }
}
