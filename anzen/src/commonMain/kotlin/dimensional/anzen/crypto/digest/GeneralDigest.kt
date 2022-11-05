package dimensional.anzen.crypto.digest

import dimensional.quic.common.arraycopy


public abstract class GeneralDigest public constructor(): ExtendedDigest {
    public companion object {
        public const val BYTE_LENGTH: Int = 64
    }

    public constructor(digest: GeneralDigest) : this() {
        copyIn(digest)
    }

    private var byteCount: Long = 0
    private var xBufOff = 0
    private val xBuf = ByteArray(4)

    override val byteLength: Int get() = BYTE_LENGTH

    override fun update(value: Byte): Digest {
        xBuf[xBufOff++] = value
        if (xBufOff == xBuf.size) {
            processWord(xBuf, 0)
            xBufOff = 0
        }

        byteCount++
        return this
    }

    override fun update(src: ByteArray, srcOff: Int, srcLen: Int): Digest {
        val len = srcLen.coerceAtLeast(0)

        /* fill the current word */
        var i = 0
        if (xBufOff != 0) {
            while (i < len) {
                xBuf[xBufOff++] = src[srcOff + i++]
                if (xBufOff == 4) {
                    processWord(xBuf, 0)
                    xBufOff = 0
                    break
                }
            }
        }

        /* process whole words. */
        val limit: Int = len - 3
        while (i < limit) {
            processWord(src, srcOff + i)
            i += 4
        }

        /* load in the remainder. */
        while (i < len) xBuf[xBufOff++] = src[srcOff + i++]
        byteCount += len

        return this
    }

    override fun reset(): Digest {
        byteCount = 0
        xBufOff = 0
        xBuf.fill(0)
        return this
    }

    public fun finish() {
        val bitLength = byteCount shl 3

        /* add the pad bytes. */
        update(128.toByte())
        while (xBufOff != 0) update(0.toByte())

        processLength(bitLength)
        processBlock()
    }

    public abstract fun processWord(src: ByteArray, srcOff: Int)

    public abstract fun processLength(bitLen: Long)

    public abstract fun processBlock()

    protected fun copyIn(other: GeneralDigest) {
        arraycopy(other.xBuf, 0, xBuf, 0, other.xBuf.size)

        xBufOff   = other.xBufOff
        byteCount = other.byteCount
    }
}
