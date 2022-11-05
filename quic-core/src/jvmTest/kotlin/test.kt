import dimensional.quic.common.extensions.hasFlag
import dimensional.quic.core.frame.QuicFrameType
import dimensional.quic.core.frame.QuicStreamFrame
import dimensional.quic.common.readVarInt
import dimensional.quic.common.writeVarInt
import io.ktor.utils.io.core.*

fun main() {
    val lol = buildPacket {
        writeVarInt(QuicStreamFrame.createFrameType(offset = true, finish = true))
    }

    println(lol.readFrameType())
}

fun ByteReadPacket.readFrameType(): QuicFrameType? {
    val value = readVarInt()?.toInt()
        ?: return null

    val type = QuicFrameType.find(value)
    if (type != null) {
        return type
    }

    // STREAM
    if (value and 0x08 == 0x08) {
        val off = value hasFlag QuicStreamFrame.OffsetFlag
        val len = value hasFlag QuicStreamFrame.LengthFlag
        val fin = value hasFlag QuicStreamFrame.FinishFlag
        println("off=$off, len=$len, fin=$fin")

        return QuicFrameType.Stream
    }

    // unknown
    return null
}
