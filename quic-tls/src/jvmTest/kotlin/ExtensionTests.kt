import dimensional.quic.tls.TlsVersion
import dimensional.quic.tls.constants.TlsPskKeyExchangeMode
import dimensional.quic.tls.constants.TlsSignatureScheme
import dimensional.quic.tls.extension.*
import dimensional.quic.tls.handshake.TlsHandshakeType
import io.ktor.utils.io.core.*
import kotlin.test.Test
import kotlin.test.assertEquals

object ExtensionTests {
    val tests = listOf(
        ExtensionTest(
            ApplicationLayerProtocolNegotiationExtension(listOf("h3")),
            "00 10 00 05 00 03 02 68 33"
        ),
        ExtensionTest(
            SupportedVersionsExtension(listOf(TlsVersion.TLS13)),
            "00 2b 00 03 02 03 04"
        ),
        ExtensionTest(
            PskKeyExchangeModesExtension(listOf(TlsPskKeyExchangeMode.psk_dhe_ke)),
            "00 2d 00 02 01 01"
        ),
        ExtensionTest(
            SignatureAlgorithmsExtension(
                listOf(
                    TlsSignatureScheme.ecdsa_secp256r1_sha256,
                    TlsSignatureScheme.rsa_pss_rsae_sha256,
                    TlsSignatureScheme.rsa_pkcs1_sha256,
                    TlsSignatureScheme.ecdsa_secp384r1_sha384,
                    TlsSignatureScheme.rsa_pss_rsae_sha384,
                    TlsSignatureScheme.rsa_pkcs1_sha384,
                    TlsSignatureScheme.rsa_pss_rsae_sha512,
                    TlsSignatureScheme.rsa_pkcs1_sha512,
                    TlsSignatureScheme.rsa_pkcs1_sha1,
                )
            ),
            "00 0d 00 14 00 12 04 03 08 04 04 01 05 03 08 05 05 01 08 06 06 01 02 01"
        )
    )

    @Test
    fun `QUIC TLS forms correct TLS extensions`() {
        for (test in tests) {
            val actual = TlsExtension.marshal(ctx, TlsHandshakeType.ClientHello, test.extension)
                .readBytes()
                .asHexDump()

            println("""
                ${test.extension.type.name}
                expected: ${test.expectedHex}
                actual  : $actual
                 
            """.trimIndent())

            assertEquals(test.expectedHex,  actual, test.extension.type.name)
        }
    }

    data class ExtensionTest(
        val extension: TlsExtension,
        val expectedHex: String
    )
}
