package dimensional.quic.common
public sealed class ByteOrder {
    public abstract fun readShort(src: ByteArray, offset: Int = 0): Short

    public abstract fun readInt(src: ByteArray, offset: Int = 0): Int

    public abstract fun readLong(src: ByteArray, offset: Int = 0): Long

    public abstract fun putShort(dst: ByteArray, value: Short, offset: Int = 0)

    public abstract fun putInt(dst: ByteArray, value: Int, offset: Int = 0)

    public abstract fun putLong(dst: ByteArray, value: Long, offset: Int = 0)
}

