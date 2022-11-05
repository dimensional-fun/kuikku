package dimensional.quic.http3.frame

import dimensional.quic.http3.Http3Connection
import dimensional.quic.common.Unmarshal
import dimensional.quic.common.asVarIntBytes
import dimensional.quic.common.readVarInt
import io.ktor.utils.io.core.*

public class Http3CancelPushFrame(
    public val pushId: Long
) : Http3Frame(Http3FrameType.CancelPush) {
    public companion object : Unmarshal<Http3Frame> {
        public override fun unmarshal(payload: ByteReadPacket): Http3CancelPushFrame? {
            val pushId = payload.readVarInt() ?: return null
            return Http3CancelPushFrame(pushId)
        }
    }

    override suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket =
        ByteReadPacket(pushId.asVarIntBytes())
}
