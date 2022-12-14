package dimensional.anzen.digest.sha

import dimensional.anzen.digest.Digest
import dimensional.quic.common.BigEndian

public class SHA384 : LongDigest() {
    public companion object {
        public const val DIGEST_SIZE: Int = 48
    }

    override val algorithmName: String get() = "SHA-384"
    override val digestSize:    Int get() = DIGEST_SIZE

    init {
        reset()
    }

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        require (dst.size - dstOff >= DIGEST_SIZE) {
            "Destination buffer is smaller than digest size, len=${dst.size} off=$dstOff: ${dst.size - dstOff} < $DIGEST_SIZE"
        }

        finish()

        BigEndian.putLong(dst, H1, dstOff +   0)
        BigEndian.putLong(dst, H2, dstOff +   8)
        BigEndian.putLong(dst, H3, dstOff +  16)
        BigEndian.putLong(dst, H4, dstOff +  24)
        BigEndian.putLong(dst, H5, dstOff +  32)
        BigEndian.putLong(dst, H6, dstOff +  40)

        reset()
        return DIGEST_SIZE
    }

    override fun reset(): Digest {
        super.reset()
        H1 = -3766243637369397544
        H2 =  7105036623409894663
        H3 = -7973340178411365097
        H4 =  1526699215303891257
        H5 =  7436329637833083697
        H6 = -8163818279084223215
        H7 = -2662702644619276377
        H8 =  5167115440072839076

        return this
    }
}
