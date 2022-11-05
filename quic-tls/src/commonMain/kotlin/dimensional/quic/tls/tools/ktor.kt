package dimensional.quic.tls.tools

import arrow.core.Option
import arrow.core.Some
import dimensional.quic.common.extensions.readFully
import dimensional.quic.common.extensions.readMedium
import dimensional.quic.common.extensions.writeMedium
import io.ktor.utils.io.core.*

public val EmptyByteArray: ByteArray = ByteArray(0)

@DslMarker
public annotation class TlsDsl

public val UByte.Companion.VALUES_RANGE: IntRange
    get() = MIN_VALUE.toInt()..MAX_VALUE.toInt()

public val UShort.Companion.VALUES_RANGE: IntRange
    get() = MIN_VALUE.toInt()..MAX_VALUE.toInt()

@TlsDsl
public fun BytePacketBuilder.writeOpaque8(bytes: ByteArray) {
    require (bytes.size in UByte.VALUES_RANGE)
    writeUByte(bytes.size.toUByte())
    writeFully(bytes)
}

@TlsDsl
public fun BytePacketBuilder.writeOpaque16(bytes: ByteArray) {
    require (bytes.size in UShort.VALUES_RANGE)
    writeUShort(bytes.size.toUShort())
    writeFully(bytes)
}

@TlsDsl
public fun BytePacketBuilder.writeOpaque16(packet: ByteReadPacket) {
    require (packet.remaining in UShort.VALUES_RANGE)
    writeUShort(packet.remaining.toUShort())
    writePacket(packet)
}

@TlsDsl
public fun BytePacketBuilder.writeOpaque16(block: BytePacketBuilder.() -> Unit) {
    writeOpaque16(buildPacket(block))
}

@TlsDsl
public fun <E> BytePacketBuilder.writeOpaque16List(items: Iterable<E>, writer: (BytePacketBuilder, E) -> Unit) {
    writeOpaque16 {
        for (item in items) writer(this, item)
    }
}

@TlsDsl
public fun BytePacketBuilder.writeOpaque24(bytes: ByteArray) {
    require (bytes.size in UShort.VALUES_RANGE)
    writeMedium(bytes.size)
    writeFully(bytes)
}

@TlsDsl
public fun BytePacketBuilder.writeOpaque24(packet: ByteReadPacket) {
    require (packet.remaining in UShort.VALUES_RANGE)
    writeMedium(packet.remaining.toInt())
    writePacket(packet)
}

@TlsDsl
public fun ByteReadPacket.readOpaque8(): ByteArray {
    val len = readUByte().toInt()
    return if (len == 0) EmptyByteArray else readFully(len)
}

@TlsDsl
public inline fun <T> ByteReadPacket.readOpaque8(block: ByteReadPacket.() -> T): T =
    block(ByteReadPacket(readOpaque8()))

@TlsDsl
public fun ByteReadPacket.readOpaque16(): ByteArray {
    val len = readShort().toInt()
    return readFully(len)
}

@TlsDsl
public inline fun <T> ByteReadPacket.readOpaque16(block: ByteReadPacket.() -> T): T =
    block(ByteReadPacket(readOpaque16()))

@TlsDsl
public inline fun <E> ByteReadPacket.readOpaque16List(reader: (opaque: ByteReadPacket) -> Option<E>): List<E> {
    val packet = ByteReadPacket(readOpaque16())

    // reader loop
    val items = mutableListOf<E>()
    reader@while (packet.isNotEmpty) {
        val item = reader(packet)
        if (item !is Some<E>) {
            break
        }

        items += item.value
    }

    return items
}

@TlsDsl
public fun ByteReadPacket.readOpaque24(): ByteArray {
    val len = readMedium()
    return readFully(len)
}

@TlsDsl
public inline fun <T> ByteReadPacket.readOpaque24(block: ByteReadPacket.() -> T): T =
    block(ByteReadPacket(readOpaque24()))

