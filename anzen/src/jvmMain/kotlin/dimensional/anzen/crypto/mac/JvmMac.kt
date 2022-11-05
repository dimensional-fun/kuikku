package dimensional.anzen.crypto.mac

import dimensional.anzen.crypto.params.CryptoParameters
import dimensional.anzen.crypto.params.KeyParameter
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac as JavaMac

@JvmInline
public value class JvmMac(public val delegate: JavaMac) : Mac {
    override val algorithmName: String
        get() = delegate.algorithm
    override val macSize: Int
        get() = delegate.macLength

    override fun initialize(params: CryptoParameters): Mac {
        require(params is KeyParameter)
        delegate.init(SecretKeySpec(params.key, params.algorithm))
        return this
    }

    override fun update(value: Byte): Mac {
        delegate.update(value)
        return this
    }

    override fun update(src: ByteArray, srcOff: Int, srcLen: Int): Mac {
        delegate.update(src, srcOff, srcLen)
        return this
    }

    override fun finalize(dst: ByteArray, dstOff: Int): Int {
        delegate.doFinal(dst, dstOff)
        return macSize
    }

    override fun reset(): Mac {
        delegate.reset()
        return this
    }
}
