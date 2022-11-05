package dimensional.quic.tls.handshake

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.TlsRecord
import dimensional.quic.tls.TlsRecordType
import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque24
import dimensional.quic.tls.tools.writeOpaque24
import io.ktor.utils.io.core.*

public sealed class TlsHandshake(
    public val type: TlsHandshakeType
) {
    public companion object : TlsMarshallingFactory<TlsHandshake> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): TlsHandshake {
            val type   = payload.readTLSHandshakeType()
            val packet = payload
                .readOpaque24()
                .let(::ByteReadPacket)

            return when (type) {
                TlsHandshakeType.ClientHello -> TlsClientHelloHandshake(ctx, packet)
                TlsHandshakeType.ServerHello -> TlsServerHelloHandshake(ctx, packet)
                else -> error("Unsupported TLS handshake type: $type")
            }
        }

        override fun marshal(ctx: TlsContext, value: TlsHandshake): ByteReadPacket = buildPacket {
            writeByte(value.type.code.toByte())
            writeOpaque24(value.marshalPayload(ctx))
        }

        public fun ByteReadPacket.readTLSHandshakeType(): TlsHandshakeType {
            val code = readByte().toInt()
            return TlsHandshakeType.values().find { it.code == code }!!
        }
    }

    public fun toRecord(ctx: TlsContext): TlsRecord = TlsRecord(
        TlsRecordType.Handshake,
        TlsVersion.TLS12,
        marshal(ctx, this)
    )

    /** */
    public abstract fun marshalPayload(ctx: TlsContext): ByteReadPacket
}
