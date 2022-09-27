package dimensional.quic.common

import kotlin.experimental.or

public object LittleEndian : ByteOrder() {
    override fun readLong(src: ByteArray, offset: Int): Long =
        (src[offset + 0].asLong() shl  0) or
        (src[offset + 1].asLong() shl  8) or
        (src[offset + 2].asLong() shl 16) or
        (src[offset + 3].asLong() shl 24) or
        (src[offset + 4].asLong() shl 32) or
        (src[offset + 5].asLong() shl 40) or
        (src[offset + 6].asLong() shl 48) or
        (src[offset + 7].asLong() shl 56)

    override fun readInt(src: ByteArray, offset: Int): Int =
        (src[offset + 0].asInt() shl  0) or
        (src[offset + 1].asInt() shl  8) or
        (src[offset + 2].asInt() shl 16) or
        (src[offset + 3].asInt() shl 24)

    override fun readShort(src: ByteArray, offset: Int): Short =
        (src[offset + 0].asShort() shl 0) or
        (src[offset + 1].asShort() shl 8)

    override fun putLong(dst: ByteArray, value: Long, offset: Int) {
        dst[offset + 0] = (value shr  0).asByte()
        dst[offset + 1] = (value shr  8).asByte()
        dst[offset + 2] = (value shr 16).asByte()
        dst[offset + 3] = (value shr 24).asByte()
        dst[offset + 4] = (value shr 32).asByte()
        dst[offset + 5] = (value shr 40).asByte()
        dst[offset + 6] = (value shr 48).asByte()
        dst[offset + 7] = (value shr 56).asByte()
    }

    override fun putInt(dst: ByteArray, value: Int, offset: Int) {
        dst[offset + 0] = (value shr  0).asByte()
        dst[offset + 1] = (value shr  8).asByte()
        dst[offset + 2] = (value shr 16).asByte()
        dst[offset + 3] = (value shr 24).asByte()
    }

    override fun putShort(dst: ByteArray, value: Short, offset: Int) {
        dst[offset + 0] = (value shr 0).asByte()
        dst[offset + 1] = (value shr 8).asByte()
    }
}

