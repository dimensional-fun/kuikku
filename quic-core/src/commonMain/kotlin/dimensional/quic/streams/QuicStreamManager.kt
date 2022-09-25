package dimensional.quic.streams

import dimensional.quic.QuicConnection
import dimensional.quic.QuicRole

public interface QuicStreamManager {
    /** The QUIC connection for this stream manager. */
    public val connection: dimensional.quic.QuicConnection

    /** The role of this stream manager, e.g. client. */
    public val role: dimensional.quic.QuicRole

    /** The number of available unidirectional streams. */
    public val availableUniStreams: Int

    /** The number of available bidirectional streams. */
    public val availableBidiStreams: Int

    /** Map of open streams associated with their ID */
    public val streams: Map<QuicStreamId, QuicStream>

    /**
     * Creates a new QUIC Stream of the specified [type].
     *
     * @param type The type of stream to create, e.g. bidirectional or unidirectional.
     * @return the newly created [QuicStream], or null if a stream could not be allocated.
     */
    public fun create(type: QuicStreamType): QuicStream?
}
