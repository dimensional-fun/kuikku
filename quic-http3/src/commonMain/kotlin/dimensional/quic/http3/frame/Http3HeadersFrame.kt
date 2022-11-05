package dimensional.quic.http3.frame

import dimensional.quic.http3.Http3Connection
import dimensional.quic.http3.qpack.QPackDecoder
import io.ktor.utils.io.core.*

public sealed class Http3HeadersFrame: Http3Frame(Http3FrameType.Headers) {
    public companion object {
        public suspend fun unmarshal(payload: ByteReadPacket, decoder: QPackDecoder): Http3HeadersFrame {
            val fields = decoder.decode(payload)
            return Received(payload.readBytes(), fields)
        }
    }

    public abstract val fields: Map<String, String>

    /** Represents a decoded HTTP3 headers frame. */
    public class Decoded(override val fields: Map<String, String>) : Http3HeadersFrame() {
        override suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket =
            connection.qpack.encoder.encode(fields)
    }

    /** */
    public class Received(
        public val encoded: ByteArray,
        override val fields: Map<String, String>
    ) : Http3HeadersFrame() {
        override suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket = ByteReadPacket(encoded)
    }

//    public data class HeaderField(val name: String, val value: String)
}
