import dimensional.quic.common.BigEndian
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

object VarIntTests {
    val bytes = ubyteArrayOf(0xC2u, 0x19u, 0x7Cu, 0x5Eu, 0xFFu, 0x14u, 0xE8u, 0x8Cu).toByteArray()
    val numer = 151_288_809_941_952_652

    @Test
    fun `NetworkOrder readVarInt correctly reads a variable-length integer`() {
//        val read = BigEndian.readVarInt(bytes)
//        assertEquals(numer, read)
    }

    @Test
    fun `NetworkOrder putVarInt correctly writes a variable-length integer`() {
//        val written = ByteArray(8)
//        BigEndian.putVarInt(written, numer)
//        assertContentEquals(bytes, written)
    }
}
