package dimensional.quic.tls.extension

import arrow.core.some
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.handshake.TlsHandshakeType
import dimensional.quic.tls.tools.*
import io.ktor.utils.io.core.*
import kotlin.jvm.JvmInline

@JvmInline
public value class TlsExtensionList(
    private val extensions: List<TlsExtension>,
) : List<TlsExtension> by extensions {
    public companion object : TlsExtensionFactory<TlsExtensionList> {
        override fun unmarshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            payload: ByteReadPacket
        ): TlsExtensionList {
            val extensions = payload.readOpaque16List { opaque ->
                TlsExtension.unmarshal(ctx, handshake, opaque).some()
            }

            return TlsExtensionList(extensions)
        }

        override fun marshal(
            ctx: TlsContext,
            handshake: TlsHandshakeType,
            value: TlsExtensionList,
        ): ByteReadPacket = buildPacket {
            writeOpaque16List(value.extensions) { opaque, extension ->
                val extensionBytes = TlsExtension.marshal(ctx, handshake, extension)
                opaque.writePacket(extensionBytes)
            }
        }
    }
}
