package dimensional.quic.core.impl.streams

import dimensional.quic.core.QuicConnection
import dimensional.quic.core.QuicRole
import dimensional.quic.core.streams.QuicStream
import dimensional.quic.core.streams.QuicStreamId
import dimensional.quic.core.streams.QuicStreamManager
import dimensional.quic.core.streams.QuicStreamType
import io.ktor.util.collections.*
import kotlinx.coroutines.sync.Semaphore

public class StreamManager(
    override val connection: QuicConnection,
    override val role: QuicRole,
    maxUniStreams: Int,
    maxBidiStreams: Int,
) : QuicStreamManager {
    private val ids = QuicStreamId.Factory(role)
    private val streamMap = ConcurrentMap<QuicStreamId, Stream>()

    /** The number of max unidirectional streams that can be created. */
    private val maxUniStreams: Int =
        QuicStreamId.calculateMaxStreams(maxUniStreams, role.other(), QuicStreamType.Unidirectional)

    /** The number of max bidirectional streams that can be created. */
    private val maxBidiStreams: Int =
        QuicStreamId.calculateMaxStreams(maxBidiStreams, role.other(), QuicStreamType.Bidirectional)

    private val openUniStreams = Semaphore(this.maxUniStreams)

    private val openBidiStreams = Semaphore(this.maxBidiStreams)

    override val availableBidiStreams: Int
        get() = openBidiStreams.availablePermits

    override val availableUniStreams: Int
        get() = openUniStreams.availablePermits

    override val streams: Map<QuicStreamId, QuicStream>
        get() = streamMap.toMap()

    override fun create(type: QuicStreamType): QuicStream? {
        val id = acquireStreamId(type)
            ?: return null

        val stream = Stream(id, connection)
        streamMap[id] = stream

        return stream
    }

    private fun acquireStreamId(type: QuicStreamType): QuicStreamId? {
        val openStreams = if (type == QuicStreamType.Bidirectional) openBidiStreams else openUniStreams
        if (!openStreams.tryAcquire()) return null

        return ids.next(type == QuicStreamType.Bidirectional)
    }
}
