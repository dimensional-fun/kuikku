import dimensional.quic.tls.TlsClientEngine
import dimensional.quic.tls.TlsClientEngineResources
import dimensional.quic.tls.TlsContext
import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.crypto.createKeyPairGenerator
import dimensional.quic.tls.extension.ApplicationLayerProtocolNegotiationExtension
import dimensional.quic.tls.extension.SupportedVersionsExtension
import dimensional.quic.tls.extension.TlsExtension
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*
import io.matthewnelson.component.encoding.base16.encodeBase16ToCharArray
import kotlin.time.measureTimedValue

val clientHello = """010002d3030357ad8a97566a717f0486e8e164ae4a4e9e4ea57069d37d2dc0fc0d33b677dd02000006130113021303010002a4000000160014000011666f6e74732e677374617469632e636f6d000d00140012040308040401050308050501080606010201001000050003026833001b0003020002002b0003020304446900050003026833002a0000003300260024001d00204efb1bb8e274de939ab4113ce583ab1faa438afe4962ee3bfbfa3736f6fcce0e00390063060480600000d1dd3c28150f6f8209da4ef701f9df976cb88000475204000000010f00070480600000080240647127027f4d010480007530030245c0040480f000000902406705048060000020048001000080ff73db0c000000015a7a2afa00000001002d00020101000a00080006001d00170018002901a70182017c02d755ff57e5eac97d17144c67c4c9d852a8a9a52339647ff7d4370866443c9de8de257140388784dcaab41da7aff5373de95ed4ffb6a57058c9ce59f60b7e133a8c347192ca848ed4a7273de6b35f1377c5db358ee639288882725af224496ba7f68c3651667783f376aed662e49621a4bd2b844d840be159e840ecebeb743ca6287a76ba94eaa4c79acf70607ae2286de95239a7881a9d891162a88f6629a9368aa2b12935a2c7c4e8a8f597a827b2d1f105771df494c32bb84216ed20d8d7409ac97abd9b095486a1d54a7f886cd04f7704753b867522fe86d0c0068d7bfbf1f2b4c3dc076da00b9076d63ab3b92582ce924faa763711d223d1719ff5c698748d4b57703cc513c88b447abba235ab91ba7349e363e56d41fc598ac9d1fca452e64839c0750157f4ed0275664eff45f66b15835a56658b6080732487ebd39940a388d83f77e0f2be213162300ed5c9de65db978072f74c40337e21ad27f94a1e5ba9c759febb048659f70de3a9b44cb953464f5f9796c4c333c7d3568024e900212012e7baeb6bd6e1879c04370153eae1712fed486642b54010f9adb258e78c4eca""".trimIndent()
val clientHello2 = """1603010200010001fc03033d81fe3362ac8f8cd47ce4b26b4ee038c3c9689ef234f1600b28a7cc0630c18520a56e0b48012146e1752da12a9652c9ba6450b4fd1c47d6526d91d04f232422af0020eaea130113021303c02bc02fc02cc030cca9cca8c013c014009c009d002f003501000193baba00000000000f000d00000a6769746875622e636f6d00170000ff01000100000a000a0008baba001d00170018000b00020100002300000010000e000c02683208687474702f312e31000500050100000000000d0012001004030804040105030805050108060601001200000033002b0029baba000100001d0020eebacfe2c3e99acc8e50b5ab400539c8a36e1fe057eca0ac4d8430fe45927b49002d00020101002b000706caca03040303001b00030200024469000500030268321a1a000100001500cd00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000""".trimIndent()
val serverHello = """0200005c0303b97aea5370e7fa63810c55223bf2db83459215b4dfc6395114cb71b25035508a00130100003400290002000000330024001d0020c342c102d8e3ea7bc171d2a8228f87264a98d108f36a6ab84e3ed85f19582f3d002b00020304""".trimIndent()

val ctx = TlsContext(TlsVersion.TLS13)

fun main() {
    val engine = TlsClientEngine(
        TlsClientEngineResources(
            "youtube.com",
            namedGroup = TlsNamedGroup.x25519
        )
    )

//    alpn()
//    supportedVersions()

//    val bytes = clientHello2.trim()
//        .lines()
//        .map { it.split(' ') }
//        .flatten()
//        .joinToString("") { it.trim() }
//        .uppercase()
//        .decodeBase16ToArray()
//        ?.let(::ByteReadPacket)
//        ?: error("Couldn't decode hex dump")
//
//    println(clientHello2)
//
//    /* unmarshalled */
//    val (record, took) = measureTimedValue {
//        TLSRecord.unmarshal(ctx, bytes)
//    }
//
//    println("$took $record")
//
//    val (handshake, hsTook) = measureTimedValue {
//        TLSHandshake.unmarshal(ctx, record.payload)
//    }
//
//    println("$hsTook $handshake")
//
//    /* marshalled */
//    val lol = handshake.toRecord(ctx)
//    val bytes2 = TLSRecord.marshal(ctx, lol)
//    println(hex(bytes2.copy().readBytes()))
//
//    val (record2, took2) = measureTimedValue {
//        TLSRecord.unmarshal(ctx, bytes2)
//    }
//
//    println("$took2 $record2")
//
//    val (handshake2, hsTook2) = measureTimedValue {
//        TLSHandshake.unmarshal(ctx, record2.payload)
//    }
//
//    println("$hsTook2 $handshake2")
}

fun alpn() {
    val extension = ApplicationLayerProtocolNegotiationExtension(
        listOf("h3")
    )

    val external = "00 10 00 05 00 03 02 68 33"
    val (kuikku, took) = measureTimedValue {
        TlsExtension.marshal(ctx, TlsHandshakeType.ClientHello, extension)
            .readBytes()
            .asHexDump()
    }

    println(
        """
        application_layer_protocol_negotiation
        external: $external
        kuikku  : $kuikku ($took)
    """.trimIndent()
    )
    assert(external == kuikku)
}

fun supportedVersions() {
    val extension = SupportedVersionsExtension(listOf(TlsVersion.TLS13))

    val external = "00 2b 00 03 02 03 04"
    val (kuikku, took) = measureTimedValue {
        TlsExtension.marshal(ctx, TlsHandshakeType.ClientHello, extension)
            .readBytes()
            .asHexDump()
    }

    println(
        """
        supported_versions
        external: $external
        kuikku  : $kuikku ($took)
    """.trimIndent()
    )
    require(external == kuikku)
}

fun ByteArray.asHexDump(): String {
    return this
        .encodeBase16ToCharArray()
        .toList()
        .chunked(2)
        .joinToString(" ") { it.joinToString("") }
        .lowercase()
}
