package dimensional.anzen.crypto.digest

import java.security.MessageDigest

@JvmInline
public value class JvmDigest(public val java: MessageDigest) : Digest {
    override val algorithmName: String
        get() = java.algorithm

    override val digestSize: Int
        get() = java.digestLength

    override fun update(value: Byte): Digest {
        java.update(value)
        return this
    }

    override fun update(src: ByteArray, srcOff: Int, srcLen: Int): Digest {
        java.update(src, srcOff, srcLen)
        return this
    }

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        return java.digest(dst, dstOff, digestSize)
    }

    override fun reset(): Digest {
        java.reset()
        return this
    }
}
