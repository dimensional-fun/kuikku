package dimensional.anzen.digest.sha

import dimensional.anzen.digest.Digest
import dimensional.quic.common.BigEndian

public class SHA512 : LongDigest() {
    public companion object {
        public const val DIGEST_SIZE: Int = 64
    }

    override val algorithmName: String get() = "SHA-512"
    override val digestSize: Int       get() = DIGEST_SIZE

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        finish()

        BigEndian.putLong(dst, H1, dstOff +  0)
        BigEndian.putLong(dst, H2, dstOff +  8)
        BigEndian.putLong(dst, H3, dstOff + 16)
        BigEndian.putLong(dst, H4, dstOff + 24)
        BigEndian.putLong(dst, H5, dstOff + 32)
        BigEndian.putLong(dst, H6, dstOff + 40)
        BigEndian.putLong(dst, H7, dstOff + 48)
        BigEndian.putLong(dst, H8, dstOff + 56)

        reset()
        return DIGEST_SIZE
    }

    override fun reset(): Digest {
        super.reset()

        H1 = 0x6a09e667f3bcc908uL.toLong()
        H2 = 0xbb67ae8584caa73buL.toLong()
        H3 = 0x3c6ef372fe94f82buL.toLong()
        H4 = 0xa54ff53a5f1d36f1uL.toLong()
        H5 = 0x510e527fade682d1uL.toLong()
        H6 = 0x9b05688c2b3e6c1fuL.toLong()
        H7 = 0x1f83d9abfb41bd6buL.toLong()
        H8 = 0x5be0cd19137e2179uL.toLong()
        return this
    }
}
