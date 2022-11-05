package dimensional.quic.http3.qpack

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

// TODO: streaming qpack encoder?
public class QPackEncoder {
    public suspend fun encode(fields: Map<String, String>): ByteReadPacket {
        val channel = ByteChannel()
        encode(channel, fields)

        return channel.readRemaining()
    }

    public suspend fun encode(channel: ByteWriteChannel, fields: Map<String, String>) {

    }
}
