package dimensional.quic.tls.crypto

import dimensional.anzen.crypto.mac.Mac
import dimensional.anzen.crypto.mac.finalize
import dimensional.anzen.crypto.params.KeyParameter
import dimensional.quic.tls.tools.EmptyByteArray
import io.ktor.utils.io.core.*
import kotlin.jvm.JvmInline
import kotlin.math.ceil

@JvmInline
public value class HKDF(private val macFactory: MacFactory) {
    public companion object {
        public val SHA256: HKDF by lazy { HKDF(digest = "SHA256") }

        public val SHA512: HKDF by lazy { HKDF(digest = "SHA512") }

        public fun performExtract(macFactory: MacFactory, salt: ByteArray, keyingMaterial: ByteArray): ByteArray {
            return macFactory.create(salt)
                .update(keyingMaterial)
                .finalize()
        }

        public fun performExpand(macFactory: MacFactory, prndKey: ByteArray, info: ByteArray, outLen: Int): ByteArray {
            val hmac = macFactory.create(prndKey)

            /* make sure the iteration count is correct. */
            val iterations = ceil(outLen.toDouble() / hmac.macSize.toDouble()).toInt()
            require(iterations <= 255) {
                "out length must be maximal 255 * hash-length; requested: $outLen bytes"
            }

            val packet = buildPacket {
                var blockN = EmptyByteArray
                var remaining = outLen
                var stepSize: Int
                for (i in 0 until iterations) {
                    hmac.update(blockN)
                    hmac.update(info)
                    hmac.update((i + 1).toByte())

                    blockN = hmac.finalize()

                    stepSize = minOf(remaining, blockN.size)
                    writeFully(blockN, 0, stepSize)

                    remaining -= stepSize
                }
            }

            return packet.readBytes()
        }
    }

    public constructor(digest: String) : this(MacFactory(digest))

    /** */
    public fun expand(psuedoRandomKey: ByteArray, info: ByteArray, outLen: Int): ByteArray {
        return performExpand(macFactory, psuedoRandomKey, info, outLen)
    }

    /** */
    public fun extract(salt: ByteArray, keyingMaterial: ByteArray): ByteArray {
        return performExtract(macFactory, salt, keyingMaterial)
    }

    /** */
    public fun extractAndExpand(salt: ByteArray, keyingMaterial: ByteArray, info: ByteArray, outLen: Int): ByteArray {
        val extracted = performExtract(macFactory, salt, keyingMaterial)
        return performExpand(macFactory, extracted, info, outLen)
    }

    public data class MacFactory(val digest: String) {
        public fun create(keyMaterial: ByteArray): Mac {
            val params = KeyParameter(keyMaterial, "")
            return Mac("HMAC$digest").initialize(params)
        }
    }
}
