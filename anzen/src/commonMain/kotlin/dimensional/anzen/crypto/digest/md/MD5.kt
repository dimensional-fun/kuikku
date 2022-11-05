package dimensional.anzen.crypto.digest.md

import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.digest.GeneralDigest
import dimensional.anzen.tools.Memoable
import dimensional.quic.common.LittleEndian
import dimensional.quic.common.arraycopy

public class MD5 : Memoable, GeneralDigest {
    private var IV = IntArray(4)
    
    private val X = IntArray(16)
    private var xOff = 0
    
    override val algorithmName: String get() = "MD5"
    
    override val digestSize:    Int    get() = DIGEST_SIZE

    public constructor() :super() {
        reset()
    }

    public constructor(other: MD5) :super() {
        reset(other)
    }

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        finish()

        LittleEndian.putInt(dst, IV[0], dstOff +  0)
        LittleEndian.putInt(dst, IV[1], dstOff +  4)
        LittleEndian.putInt(dst, IV[2], dstOff +  8)
        LittleEndian.putInt(dst, IV[3], dstOff + 12)

        reset()
        return DIGEST_SIZE
    }

    override fun reset(): Digest {
        super.reset()

        IV[0] = 0x67452301u.toInt()
        IV[1] = 0xefcdab89u.toInt()
        IV[2] = 0x98badcfeu.toInt()
        IV[3] = 0x10325476u.toInt()

        xOff = 0
        X.fill(0)

        return this
    }

    override fun processWord(src: ByteArray, srcOff: Int) {
        X[xOff++] = LittleEndian.readInt(src, srcOff)
        if (xOff == 16) processBlock()
    }

    override fun processLength(bitLen: Long) {
        if (xOff > 14) processBlock()
        X[14] = (bitLen and  0xFFFF_FFFF).toInt()
        X[15] = (bitLen ushr 32).toInt()
    }

    override fun processBlock() {
        var a = IV[0]
        var b = IV[1]
        var c = IV[2]
        var d = IV[3]

        /* Round 1 - F cycle, 16 times. */
        a = (a + F(b, c, d) + X[ 0] + 0xd76aa478u.toInt()).rotateLeft(S11) + b
        d = (d + F(a, b, c) + X[ 1] + 0xe8c7b756u.toInt()).rotateLeft(S12) + a
        c = (c + F(d, a, b) + X[ 2] + 0x242070dbu.toInt()).rotateLeft(S13) + d
        b = (b + F(c, d, a) + X[ 3] + 0xc1bdceeeu.toInt()).rotateLeft(S14) + c
        a = (a + F(b, c, d) + X[ 4] + 0xf57c0fafu.toInt()).rotateLeft(S11) + b
        d = (d + F(a, b, c) + X[ 5] + 0x4787c62au.toInt()).rotateLeft(S12) + a
        c = (c + F(d, a, b) + X[ 6] + 0xa8304613u.toInt()).rotateLeft(S13) + d
        b = (b + F(c, d, a) + X[ 7] + 0xfd469501u.toInt()).rotateLeft(S14) + c
        a = (a + F(b, c, d) + X[ 8] + 0x698098d8u.toInt()).rotateLeft(S11) + b
        d = (d + F(a, b, c) + X[ 9] + 0x8b44f7afu.toInt()).rotateLeft(S12) + a
        c = (c + F(d, a, b) + X[10] + 0xffff5bb1u.toInt()).rotateLeft(S13) + d
        b = (b + F(c, d, a) + X[11] + 0x895cd7beu.toInt()).rotateLeft(S14) + c
        a = (a + F(b, c, d) + X[12] + 0x6b901122u.toInt()).rotateLeft(S11) + b
        d = (d + F(a, b, c) + X[13] + 0xfd987193u.toInt()).rotateLeft(S12) + a
        c = (c + F(d, a, b) + X[14] + 0xa679438eu.toInt()).rotateLeft(S13) + d
        b = (b + F(c, d, a) + X[15] + 0x49b40821u.toInt()).rotateLeft(S14) + c

        /* Round 2 - G cycle, 16 times. */
        a = (a + G(b, c, d) + X[ 1] + 0xf61e2562u.toInt()).rotateLeft(S21) + b
        d = (d + G(a, b, c) + X[ 6] + 0xc040b340u.toInt()).rotateLeft(S22) + a
        c = (c + G(d, a, b) + X[11] + 0x265e5a51u.toInt()).rotateLeft(S23) + d
        b = (b + G(c, d, a) + X[ 0] + 0xe9b6c7aau.toInt()).rotateLeft(S24) + c
        a = (a + G(b, c, d) + X[ 5] + 0xd62f105du.toInt()).rotateLeft(S21) + b
        d = (d + G(a, b, c) + X[10] + 0x02441453u.toInt()).rotateLeft(S22) + a
        c = (c + G(d, a, b) + X[15] + 0xd8a1e681u.toInt()).rotateLeft(S23) + d
        b = (b + G(c, d, a) + X[ 4] + 0xe7d3fbc8u.toInt()).rotateLeft(S24) + c
        a = (a + G(b, c, d) + X[ 9] + 0x21e1cde6u.toInt()).rotateLeft(S21) + b
        d = (d + G(a, b, c) + X[14] + 0xc33707d6u.toInt()).rotateLeft(S22) + a
        c = (c + G(d, a, b) + X[ 3] + 0xf4d50d87u.toInt()).rotateLeft(S23) + d
        b = (b + G(c, d, a) + X[ 8] + 0x455a14edu.toInt()).rotateLeft(S24) + c
        a = (a + G(b, c, d) + X[13] + 0xa9e3e905u.toInt()).rotateLeft(S21) + b
        d = (d + G(a, b, c) + X[ 2] + 0xfcefa3f8u.toInt()).rotateLeft(S22) + a
        c = (c + G(d, a, b) + X[ 7] + 0x676f02d9u.toInt()).rotateLeft(S23) + d
        b = (b + G(c, d, a) + X[12] + 0x8d2a4c8au.toInt()).rotateLeft(S24) + c

        /* Round 3 - H cycle, 16 times. */
        a = (a + H(b, c, d) + X[ 5] + 0xfffa3942u.toInt()).rotateLeft(S31) + b
        d = (d + H(a, b, c) + X[ 8] + 0x8771f681u.toInt()).rotateLeft(S32) + a
        c = (c + H(d, a, b) + X[11] + 0x6d9d6122u.toInt()).rotateLeft(S33) + d
        b = (b + H(c, d, a) + X[14] + 0xfde5380cu.toInt()).rotateLeft(S34) + c
        a = (a + H(b, c, d) + X[ 1] + 0xa4beea44u.toInt()).rotateLeft(S31) + b
        d = (d + H(a, b, c) + X[ 4] + 0x4bdecfa9u.toInt()).rotateLeft(S32) + a
        c = (c + H(d, a, b) + X[ 7] + 0xf6bb4b60u.toInt()).rotateLeft(S33) + d
        b = (b + H(c, d, a) + X[10] + 0xbebfbc70u.toInt()).rotateLeft(S34) + c
        a = (a + H(b, c, d) + X[13] + 0x289b7ec6u.toInt()).rotateLeft(S31) + b
        d = (d + H(a, b, c) + X[ 0] + 0xeaa127fau.toInt()).rotateLeft(S32) + a
        c = (c + H(d, a, b) + X[ 3] + 0xd4ef3085u.toInt()).rotateLeft(S33) + d
        b = (b + H(c, d, a) + X[ 6] + 0x04881d05u.toInt()).rotateLeft(S34) + c
        a = (a + H(b, c, d) + X[ 9] + 0xd9d4d039u.toInt()).rotateLeft(S31) + b
        d = (d + H(a, b, c) + X[12] + 0xe6db99e5u.toInt()).rotateLeft(S32) + a
        c = (c + H(d, a, b) + X[15] + 0x1fa27cf8u.toInt()).rotateLeft(S33) + d
        b = (b + H(c, d, a) + X[ 2] + 0xc4ac5665u.toInt()).rotateLeft(S34) + c

        /* Round 4 - K cycle, 16 times. */
        a = (a + K(b, c, d) + X[ 0] + 0xf4292244u.toInt()).rotateLeft(S41) + b
        d = (d + K(a, b, c) + X[ 7] + 0x432aff97u.toInt()).rotateLeft(S42) + a
        c = (c + K(d, a, b) + X[14] + 0xab9423a7u.toInt()).rotateLeft(S43) + d
        b = (b + K(c, d, a) + X[ 5] + 0xfc93a039u.toInt()).rotateLeft(S44) + c
        a = (a + K(b, c, d) + X[12] + 0x655b59c3u.toInt()).rotateLeft(S41) + b
        d = (d + K(a, b, c) + X[ 3] + 0x8f0ccc92u.toInt()).rotateLeft(S42) + a
        c = (c + K(d, a, b) + X[10] + 0xffeff47du.toInt()).rotateLeft(S43) + d
        b = (b + K(c, d, a) + X[ 1] + 0x85845dd1u.toInt()).rotateLeft(S44) + c
        a = (a + K(b, c, d) + X[ 8] + 0x6fa87e4fu.toInt()).rotateLeft(S41) + b
        d = (d + K(a, b, c) + X[15] + 0xfe2ce6e0u.toInt()).rotateLeft(S42) + a
        c = (c + K(d, a, b) + X[ 6] + 0xa3014314u.toInt()).rotateLeft(S43) + d
        b = (b + K(c, d, a) + X[13] + 0x4e0811a1u.toInt()).rotateLeft(S44) + c
        a = (a + K(b, c, d) + X[ 4] + 0xf7537e82u.toInt()).rotateLeft(S41) + b
        d = (d + K(a, b, c) + X[11] + 0xbd3af235u.toInt()).rotateLeft(S42) + a
        c = (c + K(d, a, b) + X[ 2] + 0x2ad7d2bbu.toInt()).rotateLeft(S43) + d
        b = (b + K(c, d, a) + X[ 9] + 0xeb86d391u.toInt()).rotateLeft(S44) + c

        IV[0] = IV[0] + a
        IV[1] = IV[1] + b
        IV[2] = IV[2] + c
        IV[3] = IV[3] + d

        /* reset the offset and clean out the word buffer. */
        xOff = 0
        X.fill(0)
    }

    override fun copy(): MD5 = MD5(this)

    override fun reset(other: Memoable): MD5 {
        require (other is MD5)
        copyIn(other)
        return this
    }


    private fun copyIn(other: MD5) {
        super.copyIn(other)
        IV = other.IV
        arraycopy(other.X, 0, X, 0, other.X.size)
        xOff = other.xOff
    }

    public companion object {
        public const val DIGEST_SIZE: Int = 16

        /* round 1 left rotates */
        private const val S11 = 7
        private const val S12 = 12
        private const val S13 = 17
        private const val S14 = 22

        /* round 2 left rotates */
        private const val S21 = 5
        private const val S22 = 9
        private const val S23 = 14
        private const val S24 = 20

        /* round 3 left rotates */
        private const val S31 = 4
        private const val S32 = 11
        private const val S33 = 16
        private const val S34 = 23

        /* round 4 left rotates */
        private const val S41 = 6
        private const val S42 = 10
        private const val S43 = 15
        private const val S44 = 21

        /* F, G, H and I are the basic MD5 functions. */
        private fun F(u: Int, v: Int, w: Int): Int = (u and v) or (u.inv() and w)
        private fun G(u: Int, v: Int, w: Int): Int = (u and w) or (v and w.inv())
        private fun H(u: Int, v: Int, w: Int): Int = u xor v xor w
        private fun K(u: Int, v: Int, w: Int): Int = v xor (u or w.inv())
    }
}
