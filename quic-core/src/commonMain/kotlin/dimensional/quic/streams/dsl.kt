package dimensional.quic.streams

public val Long.streamId: QuicStreamId get() = QuicStreamId(this)

/**
 * Whether the supplied Stream ID belongs to a peer-initiated stream
 *
 * @param streamId The [QuicStreamId] to check.
 * @return `true` if the ID belongs to a peer-initiated stream, `false` otherwise.
 */
public fun QuicStreamManager.isPeerInitiated(streamId: QuicStreamId): Boolean = streamId.isPeerInitiated(role)

/** Whether this stream is unidirectional. */
public val QuicStream.isUnidirectional: Boolean get() = id.isUnidirectional

/** Whether this stream is bidirectional. */
public val QuicStream.isBidirectional: Boolean get() = id.isBidirectional

/** Whether this ID belongs to a unidirectional [QuicStream]. */
public val QuicStreamId.isUnidirectional: Boolean
    get() = type == QuicStreamType.Unidirectional

/** Whether this ID belongs to a bidirectional [QuicStream]. */
public val QuicStreamId.isBidirectional: Boolean
    get() = type == QuicStreamType.Bidirectional
