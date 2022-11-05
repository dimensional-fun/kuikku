package dimensional.quic.control.congestion

import dimensional.quic.core.packet.QuicPacket

public interface QuicCongestionController {
    /**
     * Record a QUIC packet for congestion control.
     *
     * @param packet The [QuicPacket] to record.
     */
    public fun record(packet: QuicPacket)

    /**
     *
     */
    public fun recordLoss(packet: QuicPacket)
}
