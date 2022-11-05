package dimensional.quic.core.frame

import dimensional.quic.common.withFlag
import dimensional.quic.core.streams.QuicStreamId

// STREAM frames, see https://www.rfc-editor.org/rfc/rfc9000.html#name-stream-frames
public data class QuicStreamFrame(
    val streamId: QuicStreamId,
    val data: ByteArray,
) : QuicFrame(QuicFrameType.Stream) {
    public companion object {
        /** Flag used to indicate the OFF field is present. */
        public const val OffsetFlag: Int = 0x04

        /** Flag used to indicate the LEN field is present. */
        public const val LengthFlag: Int = 0x02

        /** Flag used to indicate the FIN field is present. */
        public const val FinishFlag: Int = 0x01

        public fun createFrameType(offset: Boolean = false, length: Boolean = false, finish: Boolean = false): Int {
            return QuicFrameType.Stream.value
                .withFlag(if (offset) OffsetFlag else 0x00)
                .withFlag(if (length) LengthFlag else 0x00)
                .withFlag(if (finish) FinishFlag else 0x00)
        }
    }
}
