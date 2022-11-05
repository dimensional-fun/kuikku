package dimensional.anzen.crypto.mac

import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.params.CryptoParameters
import dimensional.anzen.crypto.params.KeyParameter
import dimensional.anzen.tools.Memoable
import dimensional.quic.common.arraycopy
import kotlin.experimental.xor

public class HMAC(
    public val digest: Digest,
    public val blockSize: Int,
) : Mac {
    public companion object {
        public const val IPAD: Byte = 0x36.toByte()

        public const val OPAD: Byte = 0x5C.toByte()

        public val BLOCK_SIZES: Map<String, Int> = mapOf(
            "md5"    to 64,

            "sha256" to 64,
            "sha384" to 128,
            "sha512" to 128,
        )

        public fun getBlockSize(digest: Digest): Int =
            BLOCK_SIZES[digest.algorithmName.lowercase()]!!

        private fun xorPad(pad: ByteArray, len: Int, n: Byte) {
            for (i in 0 until len) pad[i] = pad[i] xor n
        }
    }

    public constructor(digest: Digest) : this(digest, getBlockSize(digest))

    private var ipadState: Memoable? = null
    private var opadState: Memoable? = null

    private val inputPad:  ByteArray = ByteArray(blockSize)
    private val outputBuf: ByteArray = ByteArray(blockSize + digest.digestSize)

    override val algorithmName: String get() = "HMAC-${digest.algorithmName}"
    override val macSize:       Int    get() = digest.digestSize

    override fun initialize(params: CryptoParameters): HMAC {
        require (params is KeyParameter) {
            "Unsupported crypto parameters were passed."
        }

        val key    = params.key
        var keyLen = key.size
        if (keyLen > blockSize) {
            digest.update(key)
            digest.finalize(inputPad)
            keyLen = digest.digestSize
        } else {
            arraycopy(key, 0, inputPad, 0, keyLen)
        }


//        inputPad.fill(0, fromIndex = keyLen, toIndex = inputPad.size)
        for (i in keyLen until inputPad.size) inputPad[i] = 0

        arraycopy(inputPad, 0, outputBuf, 0, blockSize)

        xorPad(inputPad,  blockSize, IPAD)
        xorPad(outputBuf, blockSize, OPAD)


        if (digest is Memoable) {
            opadState = (digest as Memoable).copy()
            (opadState as Digest).update(outputBuf, 0, blockSize)
        }

        digest.update(inputPad)
        if (digest is Memoable) {
            ipadState = (digest as Memoable).copy()
        }

        return this
    }

    override fun update(value: Byte): HMAC {
        digest.update(value)
        return this
    }

    override fun update(src: ByteArray, srcOff: Int, srcLen: Int): HMAC {
        digest.update(src, srcOff, srcLen)
        return this
    }

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        digest.finalize(outputBuf, blockSize)

        if (opadState != null) {
            (digest as Memoable).reset(opadState!!)
            digest.update(outputBuf, blockSize, digest.digestSize)
        } else {
            digest.update(outputBuf)
        }

        val len = digest.finalize(dst, dstOff)

//        outputBuf.fill(0, fromIndex = blockSize, toIndex = outputBuf.size)
        for (i in blockSize until outputBuf.size) outputBuf[i] = 0

        if (ipadState != null) {
            (digest as Memoable).reset(ipadState!!)
        } else {
            digest.update(inputPad)
        }

        return len
    }

    override fun reset(): HMAC {
        if (ipadState != null) {
            (digest as Memoable).reset(ipadState!!)
        } else {
            digest.reset()
            digest.update(inputPad)
        }

        return this
    }
}
