package dimensional.quic.streams

import dimensional.quic.QuicRole
import dimensional.quic.impl.tools.hasAnyFlags
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlin.jvm.JvmInline

@JvmInline
public value class QuicStreamId(public val value: Long) {
    public companion object {
        public const val CLIENT_INITIATED: Int = 0x00

        public const val SERVER_INITIATED: Int = 0x01

        /** Calculate the max streams for the specified peer-role and stream type. */
        public fun calculateMaxStreams(
            initial: Int,
            peer: QuicRole,
            type: QuicStreamType
        ): Int = (initial * 4) + when (peer) {
            QuicRole.Client -> if (type.isUni())  2 else 3
            QuicRole.Server -> if (type.isBidi()) 1 else 0
        }
    }

    private val isUni get() = value and 0x0002L == 0x0002L

    /** The [QUIC role][QuicRole] of this stream id */
    public val role: QuicRole
        get() = if (value.hasAnyFlags(STREAM_SERVER_BIDI, STREAM_SERVER_UNI)) {
        QuicRole.Server
    } else {
        QuicRole.Client
    }

    /** The [type of stream][QuicStreamType] this ID belongs to */
    public val type: QuicStreamType get() = if (isUni) QuicStreamType.Unidirectional else QuicStreamType.Bidirectional

    /**
     * Whether this stream id belongs to a peer-initiated stream
     *
     * @param role Our QUIC role.
     */
    public fun isPeerInitiated(role: QuicRole): Boolean {
        return value.mod(2) == if (role == QuicRole.Client) 1 else 0
    }

    override fun toString(): String = "$value"

    /**
     * A factory for [stream ids][QuicStreamId]
     */
    public class Factory(role: QuicRole) {
        /** The initial values of all generated Stream IDs */
        private val initial: Int = if (role == QuicRole.Server) SERVER_INITIATED else CLIENT_INITIATED

        /** The synchronization object*/
        private val lock = SynchronizedObject()

        /* The next Stream ID indices for bidirectional & unidirectional streams */
        private var nextBidirectional:  Long = 0L
        private var nextUnidirectional: Long = 0L

        /** Generates the next Stream ID */
        public fun next(bidirectional: Boolean): QuicStreamId =
            QuicStreamId((incr(bidirectional) shl 2) + initial + if (!bidirectional) 2 else 0)

        private fun incr(bidirectional: Boolean): Long = synchronized(lock) {
            if (bidirectional) nextBidirectional++ else nextUnidirectional++
        }
    }
}
