package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.writeOpaque16
import io.ktor.utils.io.core.*

public abstract class TlsExtension(public val type: TlsExtensionType) {
    public companion object : TlsExtensionFactory<TlsExtension> {
        /** Marshals this TLS handshake extension into a byte packet. */
        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: TlsExtension,
        ): ByteReadPacket = buildPacket {
            writeShort(extension.type.code)
            writeOpaque16(extension.marshalPayload(ctx, handshake))
        }

        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket,
        ): TlsExtension {
            val typeCode = payload.readShort()

            val type   = TlsExtensionType[typeCode]
            val packet = payload
                .readOpaque16()
                .let(::ByteReadPacket)

            return when (type) {
                TlsExtensionType.ServerName ->
                    ServerNameExtension.unmarshal(ctx, handshake, packet)

                // TLSExtensionType.MaxFragmentLength ->

                TlsExtensionType.SupportedGroups ->
                    SupportedGroupsExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.SignatureAlgorithms ->
                    SignatureAlgorithmsExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.ApplicationLayerProtocolNegotiation ->
                    ApplicationLayerProtocolNegotiationExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.PreSharedKey ->
                    PreSharedKeyExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.EarlyData ->
                    EarlyDataExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.SupportedVersions ->
                    SupportedVersionsExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.PskKeyExchangeModes ->
                    PskKeyExchangeModesExtension.unmarshal(ctx, handshake, packet)

                TlsExtensionType.KeyShare ->
                    KeyShareExtension.unmarshal(ctx, handshake, packet)

                else -> UnknownExtension(typeCode, packet)
            }
        }

        public fun ByteReadPacket.readTLSExtensionType(): TlsExtensionType = TlsExtensionType[readShort()]
    }

    /** Marshals the payload of this TLS handshake extension */
    public abstract fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket
}
