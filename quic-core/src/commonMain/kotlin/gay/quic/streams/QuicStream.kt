package gay.quic.streams

import gay.quic.QuicConnection

public interface QuicStream {
    /**
     * The ID of this QUIC stream. This field can also be used to retrieve
     * information on this stream, e.g. whether it is unidirectional.
     */
    public val id: QuicStreamId

    /**
     * The [QUIC connection][QuicConnection] this stream belongs to.
     */
    public val connection: QuicConnection
}
