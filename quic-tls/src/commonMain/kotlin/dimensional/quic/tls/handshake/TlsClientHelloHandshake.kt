package dimensional.quic.tls.handshake

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.constants.TlsCipherSuite
import dimensional.quic.tls.extension.TlsExtensionList
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque16
import dimensional.quic.tls.tools.readOpaque8
import io.ktor.utils.io.core.*

public data class TlsClientHelloHandshake(
    val random: ByteArray,
    val cipherSuites: List<TlsCipherSuite>,
    val extensions: TlsExtensionList
) : TlsHandshake(TlsHandshakeType.ClientHello) {
    public companion object : TlsMarshallingFactory<TlsClientHelloHandshake> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): TlsClientHelloHandshake {
            /* protocol version */
            payload.readShort()

            /* random */
            val random = ByteArray(32)
            payload.readFully(random)

            /* legacy_session_id */
            payload.readOpaque8()

            /* cipher_suites */
            val cipherSuites = payload.readOpaque16 {
                List(remaining.toInt() / 2) {
                    val code = readShort()
                    TlsCipherSuite.byCode(code)
                }.filterNotNull()
            }

            /* legacy_compression_methods */
            payload.readByte()
            payload.readByte()

            /* extensions */
            val extensions = TlsExtensionList.unmarshal(
                ctx,
                TlsHandshakeType.ClientHello,
                payload
            )

            // construct ClientHello handshake
            return TlsClientHelloHandshake(random, cipherSuites, extensions)
        }

        override fun marshal(ctx: TlsContext, value: TlsClientHelloHandshake): ByteReadPacket = buildPacket {
            writeShort(TlsVersion.TLS12.code)

            /* random */
            writeFully(value.random)

            /* legacy_session_id */
            writeByte(0)

            /* cipher_suites */
            writeShort((value.cipherSuites.size * 2).toShort())
            for (cipherSuite in value.cipherSuites) writeShort(cipherSuite.code)

            /* legacy_compression_methods */
            writeByte(1)
            writeByte(0) // always null.

            /* extensions */
            writePacket(TlsExtensionList.marshal(ctx, TlsHandshakeType.ClientHello, value.extensions))
        }
    }

    override fun marshalPayload(ctx: TlsContext): ByteReadPacket = marshal(ctx, this)
}
