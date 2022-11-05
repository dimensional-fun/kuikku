package dimensional.quic.tls.handshake

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.constants.TlsCipherSuite
import dimensional.quic.tls.extension.TlsExtensionList
import dimensional.quic.tls.tools.TlsMarshallingFactory
import io.ktor.utils.io.core.*

public data class TlsServerHelloHandshake(
    val random: ByteArray,
    val cipherSuite: TlsCipherSuite,
    val extensions: TlsExtensionList
) : TlsHandshake(TlsHandshakeType.ServerHello) {
    public companion object : TlsMarshallingFactory<TlsServerHelloHandshake> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): TlsServerHelloHandshake {
            /* protocol version */
            payload.readShort()

            /* random */
            val random = ByteArray(32)
            payload.readFully(random)

            /* legacy_session_id */
            payload.readByte()

            /* cipher suite */
            val cipherSuiteCode = payload.readShort()
            val cipherSuite = TlsCipherSuite.byCode(cipherSuiteCode)
                ?: error("Unknown TLS cipher suite: $cipherSuiteCode")

            /* legacy_compression_method */
            payload.readByte()

            /* extensions */
            val extensions = TlsExtensionList.unmarshal(ctx, TlsHandshakeType.ServerHello, payload)

            // construct server hello handshake
            return TlsServerHelloHandshake(random, cipherSuite, extensions)
        }

        override fun marshal(ctx: TlsContext, value: TlsServerHelloHandshake): ByteReadPacket = buildPacket {
            /* tls version */
            writeShort(TlsVersion.TLS12.code)
            /* random */
            writeFully(value.random)
            /* legacy_session_id */
            writeByte(0)
            /* cipher_suite */
            writeShort(value.cipherSuite.code)
            /* legacy_compression_method */
            writeByte(0)
            /* extensions */
            writePacket(TlsExtensionList.marshal(ctx, TlsHandshakeType.ServerHello, value.extensions))
        }
    }

    override fun marshalPayload(ctx: TlsContext): ByteReadPacket = marshal(ctx, this)
}
