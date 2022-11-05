package dimensional.quic.core.frame

// todo: see if we can just store frame types in the Quic constants object

public enum class QuicFrameType(private val head: Int, private val tail: Int? = null) {
    Padding            (0x00),
    Ping               (0x01),
    Ack                (0x02),
    AckCumulative      (0x03),
    ResetStream        (0x04),
    StopSending        (0x05),
    Crypto             (0x06),
    NewToken           (0x07),
    Stream             (0x08, 0x0f),
    MaxData            (0x10),
    MaxStreamData      (0x11),
    BiMaxStreams       (0x12),
    UniMaxStreams      (0x13),
    DataBlocked        (0x14),
    StreamDataBlocked  (0x15),
    BiStreamsBlocked   (0x16),
    UniStreamsBlocked  (0x17),
    NewConnectionId    (0x18),
    RetireConnectionId (0x19),
    PathChallenge      (0x1a),
    PathResponse       (0x1b),
    ConnectionClose    (0x1c, 0x1d),
    HandshakeDone      (0x1e),
    ;

    public val value: Int
        get() = head

    public fun accepts(value: Int): Boolean {
        if (tail == null) return head == value
        return value <= tail && value >= head
    }

    public companion object {
        public fun find(value: Int): QuicFrameType? = values().find { it.accepts(value) }
    }
}
