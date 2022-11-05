/**
 * This was ported from https://bitbucket.org/pjtr/kwik/src/master/src/main/java/net/luminis/quic/VariableLengthInteger.java
 * to make work with Ktor & Kotlin
 */

package dimensional.quic.common

import dimensional.quic.common.extensions.asByte
import dimensional.quic.common.extensions.asInt
import dimensional.quic.common.extensions.asLong
import io.ktor.utils.io.core.*
import kotlin.experimental.and
import kotlin.experimental.or

internal fun Long.asVarIntBytes(): ByteArray {
    val ba = ByteArray(varIntByteLength)
    BigEndian.putVarInt(ba, this, 0)

    return ba
}

internal val Long.varIntByteLength: Int
    get() = when {
        this <= 63 -> 1
        this <= 16383 -> 2
        this <= 1073741823 -> 4
        else -> 8
    }

public fun ByteReadPacket.readVarInt(): Long? {
    val fb = tryPeek()
    if (fb == -1) {
        return null
    }

    return when (fb shr 6) {
        0 -> readByte().asLong()
        1 -> (readShort() and 0x3FFF).toLong()
        2 -> (readInt()   and 0x3FFF_FFFF).toLong()
        3 -> (readLong()  and 0x3FFF_FFFF_FFFF_FFFFL)
        else -> error("Invalid variable-length integer.")
    }
}

public fun BytePacketBuilder.writeVarInt(value: Int) {
    writeVarInt(value.toLong())
}

public fun BytePacketBuilder.writeVarInt(value: Long) {
    val dst = ByteArray(value.varIntByteLength)
    BigEndian.putVarInt(dst, value)
    writeFully(dst)
}

internal fun BigEndian.readVarInt(src: ByteArray, offset: Int = 0): Long {
    val fb = src[offset]
    return when (fb.asInt() shr 6) {
        0 -> fb.asLong()
        1 -> (readShort(src, offset) and 0x3FFF).toLong()
        2 -> (readInt(  src, offset) and 0x3FFF_FFFF).toLong()
        3 -> (readLong( src, offset) and 0x3FFF_FFFF_FFFF_FFFFL)
        else -> error("Invalid variable-length integer.")
    }
}

internal fun BigEndian.putVarInt(
    dst: ByteArray,
    value: Long,
    offset: Int = 0,
): Int {
    val size = value.varIntByteLength
    when (size) {
        1 -> {
            dst[offset] = value.asByte()
        }

        2 -> {
            dst[offset + 0] = (value / 256 or 0x40).toByte()
            dst[offset + 1] = (value % 256).toByte()
        }

        4 -> {
            putInt(dst, value.toInt(), offset)
            dst[offset] = dst[offset] or 0x80.toByte()
        }

        else -> {
            putLong(dst, value, offset)
            dst[offset] = dst[offset] or 0xC0.toByte()
        }
    }

    return size
}
