package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*

public class UnknownExtension(
    public val typeCode: Short,
    public val packet: ByteReadPacket,
) : TlsExtension(TlsExtensionType[typeCode]) {
    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket = packet.copy()

    override fun toString(): String = "UnknownExtension(typeCode=$typeCode, packet=$packet)"
}
