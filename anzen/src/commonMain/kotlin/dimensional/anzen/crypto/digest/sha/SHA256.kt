package dimensional.anzen.crypto.digest.sha

import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.digest.GeneralDigest
import dimensional.anzen.tools.Memoable
import dimensional.anzen.tools.numbersToInts
import dimensional.quic.common.BigEndian
import dimensional.quic.common.arraycopy

public class SHA256 : Memoable, GeneralDigest {
    public companion object {
        public const val DIGEST_SIZE: Int = 32


        // SHA-256 Constants
        // (represent the first 32 bits of the fractional parts of the
        // cube roots of the first sixty-four prime numbers)
        public val K: IntArray = numbersToInts(
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        )

        /* SHA-256 functions */
        private fun Ch(x: Int, y: Int, z: Int): Int  = (x and y) xor (x.inv() and z)
        private fun Maj(x: Int, y: Int, z: Int): Int = (x and y) or (z and (x xor y))

        private fun Sum0(x: Int): Int   = ((x ushr  2) or (x shl 30)) xor ((x ushr 13) or (x shl 19)) xor ((x ushr 22) or (x shl 10))
        private fun Sum1(x: Int): Int   = ((x ushr  6) or (x shl 26)) xor ((x ushr 11) or (x shl 21)) xor ((x ushr 25) or (x shl 7))

        private fun Theta0(x: Int): Int = ((x ushr  7) or (x shl 25)) xor ((x ushr 18) or (x shl 14)) xor (x ushr  3)
        private fun Theta1(x: Int): Int = ((x ushr 17) or (x shl 15)) xor ((x ushr 19) or (x shl 13)) xor (x ushr 10)
    }

    public constructor() : super() {
        reset()
    }

    public constructor(other: SHA256) : super() {
        reset(other)
    }

    private var H1 = 0
    private var H2 = 0
    private var H3 = 0
    private var H4 = 0
    private var H5 = 0
    private var H6 = 0
    private var H7 = 0
    private var H8 = 0

    private val X = IntArray(64)
    private var xOff = 0

    override val algorithmName: String get() = "SHA256"

    override val digestSize: Int get() = DIGEST_SIZE

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        finish()

        BigEndian.putInt(dst, H1, dstOff +  0)
        BigEndian.putInt(dst, H2, dstOff +  4)
        BigEndian.putInt(dst, H3, dstOff +  8)
        BigEndian.putInt(dst, H4, dstOff + 12)
        BigEndian.putInt(dst, H5, dstOff + 16)
        BigEndian.putInt(dst, H6, dstOff + 20)
        BigEndian.putInt(dst, H7, dstOff + 24)
        BigEndian.putInt(dst, H8, dstOff + 28)

        reset()
        return DIGEST_SIZE
    }

    override fun reset(): Digest {
        super.reset()

        // SHA-256 initial hash value
        // * The first 32 bits of the fractional parts of the square roots
        // * of the first eight prime numbers
        H1 = 0x6a09e667
        H2 = 0xbb67ae85.toInt()
        H3 = 0x3c6ef372
        H4 = 0xa54ff53a.toInt()
        H5 = 0x510e527f
        H6 = 0x9b05688c.toInt()
        H7 = 0x1f83d9ab
        H8 = 0x5be0cd19

        xOff = 0
        X.fill(0)

        return this
    }

    override fun processWord(src: ByteArray, srcOff: Int) {
        X[xOff] = BigEndian.readInt(src, srcOff)
        if (++xOff == 16) processBlock()
    }

    override fun processLength(bitLen: Long) {
        if (xOff > 14) processBlock()
        X[14] = (bitLen ushr 32).toInt()
        X[15] = (bitLen and 0xFFFF_FFFF).toInt()
    }

    override fun processBlock() {
        /* expand 16 word block into 64 word blocks. */
        for (t in 16..63) {
            X[t] = Theta1(X[t - 2]) + X[t - 7] + Theta0(X[t - 15]) + X[t - 16]
        }

        /* set up working variables. */
        var a = H1
        var b = H2
        var c = H3
        var d = H4
        var e = H5
        var f = H6
        var g = H7
        var h = H8

        var t = 0
        for (i in 0 until 8) {
            // t = 8 * i
            h += Sum1(e) + Ch(e, f, g) + K[t] + X[t]
            d += h
            h += Sum0(a) + Maj(a, b, c)
            ++t

            // t = 8 * i + 1
            g += Sum1(d) + Ch(d, e, f) + K[t] + X[t]
            c += g
            g += Sum0(h) + Maj(h, a, b)
            ++t

            // t = 8 * i + 2
            f += Sum1(c) + Ch(c, d, e) + K[t] + X[t]
            b += f
            f += Sum0(g) + Maj(g, h, a)
            ++t

            // t = 8 * i + 3
            e += Sum1(b) + Ch(b, c, d) + K[t] + X[t]
            a += e
            e += Sum0(f) + Maj(f, g, h)
            ++t

            // t = 8 * i + 4
            d += Sum1(a) + Ch(a, b, c) + K[t] + X[t]
            h += d
            d += Sum0(e) + Maj(e, f, g)
            ++t

            // t = 8 * i + 5
            c += Sum1(h) + Ch(h, a, b) + K[t] + X[t]
            g += c
            c += Sum0(d) + Maj(d, e, f)
            ++t

            // t = 8 * i + 6
            b += Sum1(g) + Ch(g, h, a) + K[t] + X[t]
            f += b
            b += Sum0(c) + Maj(c, d, e)
            ++t

            // t = 8 * i + 7
            a += Sum1(f) + Ch(f, g, h) + K[t] + X[t]
            e += a
            a += Sum0(b) + Maj(b, c, d)
            ++t
        }

        H1 += a
        H2 += b
        H3 += c
        H4 += d
        H5 += e
        H6 += f
        H7 += g
        H8 += h

        /* reset the offset and clean out the word buffer. */
        xOff = 0
        for (i in 0 until 16) X[i] = 0
    }

    override fun copy(): SHA256 {
        return SHA256(this)
    }

    override fun reset(other: Memoable): SHA256 {
        require (other is SHA256)
        copyIn(other)
        return this
    }

    private fun copyIn(other: SHA256) {
        super.copyIn(other)
        H1 = other.H1
        H2 = other.H2
        H3 = other.H3
        H4 = other.H4
        H5 = other.H5
        H6 = other.H6
        H7 = other.H7
        H8 = other.H8

        arraycopy(other.X, 0, X, 0, other.X.size)
        xOff = other.xOff
    }
}
