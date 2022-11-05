package dimensional.quic.core.packet

import kotlinx.datetime.Instant

/** A recording of a previously sent QUIC packet. */
public data class QuicPacketRecording(
    val sent: Instant,
    val packet: QuicPacket
)
