package dimensional.quic.core.impl.streams

import dimensional.quic.core.QuicConnection
import dimensional.quic.core.streams.QuicStream
import dimensional.quic.core.streams.QuicStreamId

public class Stream(
    override val id: QuicStreamId,
    override val connection: QuicConnection,
) : QuicStream {

}
