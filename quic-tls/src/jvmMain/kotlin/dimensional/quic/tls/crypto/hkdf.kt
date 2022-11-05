package dimensional.quic.tls.crypto

import dimensional.quic.tls.tools.writeOpaque8
import io.ktor.utils.io.core.*

private val labelPrefixBytes = "tls13 ".toByteArray(Charsets.ISO_8859_1)

/* https://www.rfc-editor.org/rfc/rfc8446#section-7.1 */
public fun createHKDFLabel(
    label: String,
    context: ByteArray,
    length: Short,
): ByteReadPacket = buildPacket {
    writeShort(length)
    writeOpaque8(labelPrefixBytes + label.toByteArray(Charsets.ISO_8859_1))
    writeOpaque8(context)
}

/* https://www.rfc-editor.org/rfc/rfc8446#section-7.1 */
public fun HKDF.expandLabel(
    secret: ByteArray,
    label: String,
    context: ByteArray,
    length: Short,
): ByteArray {
    val hkdfLabel = createHKDFLabel(label, context, length)
    return expand(secret, hkdfLabel.readBytes(), length.toInt())
}
