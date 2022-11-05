package dimensional.quic.core.streams

public const val STREAM_CLIENT_BIDI: Int = 0x00
public const val STREAM_SERVER_BIDI: Int = 0x01
public const val STREAM_CLIENT_UNI:  Int = 0x02
public const val STREAM_SERVER_UNI:  Int = 0x03

public inline val Long.streamId: QuicStreamId get() = QuicStreamId(this)

/**
 * Whether the supplied Stream ID belongs to a peer-initiated stream
 *
 * @param streamId The [QuicStreamId] to check.
 * @return `true` if the ID belongs to a peer-initiated stream, `false` otherwise.
 */
public inline fun QuicStreamManager.isPeerInitiated(streamId: QuicStreamId): Boolean = streamId.isPeerInitiated(role)

/** Whether this stream is unidirectional. */
public inline val QuicStream.isUnidirectional: Boolean get() = id.isUnidirectional

/** Whether this stream is bidirectional. */
public inline val QuicStream.isBidirectional: Boolean get() = id.isBidirectional

/** Whether this ID belongs to a unidirectional [QuicStream]. */
public inline val QuicStreamId.isUnidirectional: Boolean get() = type == QuicStreamType.Unidirectional

/** Whether this ID belongs to a bidirectional [QuicStream]. */
public inline val QuicStreamId.isBidirectional: Boolean get() = type == QuicStreamType.Bidirectional
