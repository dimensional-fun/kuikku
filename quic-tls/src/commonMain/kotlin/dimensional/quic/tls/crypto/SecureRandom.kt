package dimensional.quic.tls.crypto

import dimensional.quic.common.BigEndian
import dimensional.quic.common.arraycopy
import kotlin.random.Random

internal expect fun fillRandomBytes(dst: ByteArray)

public object SecureRandom : Random() {
    private val nextInt: Int
        get() = BigEndian.readInt(nextBytes(4))

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        val r = ByteArray(toIndex - fromIndex)
        fillRandomBytes(r)
        arraycopy(r, 0, array, fromIndex, r.size)

        return array
    }

    override fun nextBits(bitCount: Int): Int = nextInt and ((1 shl bitCount) - 1)
}
