package dimensional.quic.http3.qpack

import dimensional.quic.common.and
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.math.pow

public class QPackDecoder {
    public companion object {
        /** Prefixed-integer reader courtesy of [pjtr/qpack](https://bitbucket,org/pjtr/qpack) */
        public suspend fun ByteReadChannel.readPrefixedInt(length: Int): Long {
            val maxPrefix: Int = (2.0.pow(length) - 1).toInt()
            val initialValue: Int = (readByte() and maxPrefix).toInt()
            if (initialValue < maxPrefix) {
                return initialValue.toLong()
            }

            var value = initialValue.toLong()
            var factor = 0
            var next: Byte
            do {
                next = readByte()
                value += (next.toInt() and 0x7f shl factor).toLong()
                factor += 7
            } while (next.toInt() and 0x80 == 0x80)

            return value
        }
    }

    public suspend fun decode(packet: ByteReadPacket): Map<String, String> =
        decode(ByteReadChannel(packet.readBytes()))

    public suspend fun decode(packet: ByteReadChannel): Map<String, String> {
        val fields = mutableMapOf<String, String>()
        // TODO: qpack decoding

        return fields.toMap()
    }
}
