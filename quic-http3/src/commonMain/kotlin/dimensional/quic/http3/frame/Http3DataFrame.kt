package dimensional.quic.http3.frame

import dimensional.quic.http3.Http3Connection
import dimensional.quic.common.Unmarshal
import io.ktor.utils.io.core.*

public data class Http3DataFrame(val payload: ByteArray) : Http3Frame(Http3FrameType.Data) {
    public companion object : Unmarshal<Http3Frame> {
        /** */
        override fun unmarshal(payload: ByteReadPacket): Http3DataFrame = Http3DataFrame(payload.readBytes())
    }

    override suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket = ByteReadPacket(payload)
}
