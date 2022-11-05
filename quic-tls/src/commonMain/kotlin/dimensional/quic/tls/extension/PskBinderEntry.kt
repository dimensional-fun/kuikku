package dimensional.quic.tls.extension

import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.tools.TlsMarshallingFactory
import dimensional.quic.tls.tools.readOpaque8
import io.ktor.utils.io.core.*
import kotlin.jvm.JvmInline

@JvmInline
public value class PskBinderEntry(public val hmac: ByteArray) {
    public companion object : TlsMarshallingFactory<PskBinderEntry> {
        override fun unmarshal(ctx: TlsContext, payload: ByteReadPacket): PskBinderEntry =
            PskBinderEntry(payload.readOpaque8())

        override fun marshal(ctx: TlsContext, value: PskBinderEntry): ByteReadPacket = buildPacket {
            writeByte(value.hmac.size.toByte())
            writeFully(value.hmac)
        }
    }

    public val estimatedSize: Int get() = 1 + hmac.size
}
