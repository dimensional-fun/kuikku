package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*

public data class SupportedVersionsExtension(
    val versions: List<TlsVersion>
) : TlsExtension(TlsExtensionType.SupportedVersions) {
    public companion object : TlsExtensionFactory<SupportedVersionsExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): SupportedVersionsExtension {
            payload.readByte()

            val versions = List((payload.remaining / 2).toInt()) {
                val code = payload.readShort()
                TlsVersion.byCode(code)
            }

            return SupportedVersionsExtension(versions)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: SupportedVersionsExtension
        ): ByteReadPacket = buildPacket {
            if (handshake == TlsHandshakeType.ClientHello) {
                writeByte((extension.versions.size * 2).toByte())
                for (version in extension.versions) {
                    writeShort(version.code)
                }
            } else {
                writeShort(ctx.version.code)
            }
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
