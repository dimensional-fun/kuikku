package dimensional.quic.common.extensions

import dimensional.quic.common.BigEndian
import io.ktor.utils.io.core.*

public fun ByteReadPacket.readFully(n: Int = 0): ByteArray {
    val bytes = ByteArray(n)
    readFully(bytes)

    return bytes
}
public fun ByteReadPacket.readPacket(n: Int = 0): ByteReadPacket {
    return ByteReadPacket(readFully(n))
}

// TODO: Maybe find a more native way of doing this?

public fun BytePacketBuilder.writeMedium(value: Int) {
    val lol = ByteArray(3)
    BigEndian.putMedium(lol, value, 0)
    writeFully(lol)
}

public fun ByteReadPacket.readMedium(): Int {
    val lol = ByteArray(3)
    readFully(lol)
    return BigEndian.readMedium(lol, 0)
}
