package dimensional.quic.core.packet

import dimensional.quic.core.frame.QuicFrame

public sealed class QuicPacket {
    public abstract val packetNumber: QuicPacketNumber

    public abstract val frames: List<QuicFrame>
}
