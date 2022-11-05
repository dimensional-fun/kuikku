package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.constants.TlsSignatureScheme
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.readOpaque16List
import dimensional.quic.tls.tools.writeOpaque16List
import io.ktor.utils.io.core.*

public data class SignatureAlgorithmsExtension(
    val schemes: List<TlsSignatureScheme>
) : TlsExtension(TlsExtensionType.SignatureAlgorithms) {
    public companion object : TlsExtensionFactory<SignatureAlgorithmsExtension> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): SignatureAlgorithmsExtension {
            val schemes = payload.readOpaque16List { opaque ->
                val code = opaque.readShort()
                TlsSignatureScheme.byCode(code).some()
            }

            return SignatureAlgorithmsExtension(schemes)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            extension: SignatureAlgorithmsExtension
        ): ByteReadPacket = buildPacket {
            writeOpaque16List(extension.schemes) { opaque, scheme -> opaque.writeShort(scheme.code) }
        }
    }

    override fun marshalPayload(ctx: TlsContext, handshake: TlsHandshakeType): ByteReadPacket =
        marshal(ctx, handshake, this)
}
