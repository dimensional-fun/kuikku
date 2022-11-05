package dimensional.quic.http3.frame

import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import dimensional.quic.http3.Http3
import dimensional.quic.http3.Http3Connection
import dimensional.quic.common.readVarInt
import dimensional.quic.common.writeVarInt
import io.ktor.utils.io.core.*

public sealed class Http3Frame(
    /** The frame type */
    public val type: Http3FrameType
) {
    public companion object {
        public fun unmarshal(packet: ByteReadPacket): Validated<Int, Http3UnmarshalledFrame> {
            val type = packet.readVarInt()
                ?.let { Http3FrameType[it] }
                ?: return Http3.Errors.FrameError.invalid()

            val length = packet.readVarInt()
                ?: return Http3.Errors.FrameError.invalid()

            // TODO: Return FrameError error code for invalid size.
            val unmarshalled = Http3UnmarshalledFrame(
                type,
                packet.readBytes(length.toInt())
            )

            return unmarshalled.valid()
        }
    }

    /** Marshals this HTTP3 frame */
    public suspend fun marshal(connection: Http3Connection): ByteReadPacket = buildPacket {
        writeVarInt(type.value)

        val payload = marshalPayload(connection)
        writeVarInt(payload.remaining)
        writePacket(payload)
    }

    /** Returns the marshalled payload of this frame, excluding the type and payload length. */
    internal abstract suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket
}
