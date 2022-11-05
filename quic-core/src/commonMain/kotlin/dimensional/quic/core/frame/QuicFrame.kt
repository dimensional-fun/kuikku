package dimensional.quic.core.frame

public sealed class QuicFrame(public val type: QuicFrameType) {
    // PADDING frames
    public object Padding : QuicFrame(QuicFrameType.Padding)

    // PING frames
    public object Ping : QuicFrame(QuicFrameType.Ping)

    // ACK frames (TODO)

    // RESET_STREAM frames (TODO)

    // STOP_SENDING frames (TODO)

    // CRYPTO frames, see https://www.rfc-editor.org/rfc/rfc9000.html#name-crypto-frames
    public data class Crypto(
        val offset: Int,
        val length: Int,
        val data: ByteArray
    ) : QuicFrame(QuicFrameType.Crypto)

    // NEW_TOKEN frames, see https://www.rfc-editor.org/rfc/rfc9000.html#name-new_token-frames
    public data class NewToken(
        val length: Long,
        val token: ByteArray
    ) : QuicFrame(QuicFrameType.NewToken)
}
