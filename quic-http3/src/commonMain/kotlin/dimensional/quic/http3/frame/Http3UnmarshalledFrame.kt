package dimensional.quic.http3.frame

/** Represents an unmarshalled HTTP3 frame. */
public data class Http3UnmarshalledFrame(
    val type: Http3FrameType,
    val payload: ByteArray
)
