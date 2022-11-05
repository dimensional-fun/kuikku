package dimensional.quic.core.packet

/**
 * QUIC packet numbers are divided into 3 different spaces.
 *
 * - See [RFC9000#packet-numbers](https://www.rfc-editor.org/rfc/rfc9000.html#name-packet-numbers)
 */
public enum class QuicPacketNumberSpace {
    /** All initial packages are in this space. */
    Initial,

    /** All handshake packets are in this space. */
    Handshake,

    /** All 0-RTT and 1-RTT packets are in this space. */
    ApplicationData
}
