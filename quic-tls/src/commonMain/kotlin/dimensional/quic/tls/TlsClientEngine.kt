package dimensional.quic.tls

import dimensional.quic.tls.constants.TlsNamedGroup
import dimensional.quic.tls.crypto.*
import dimensional.quic.tls.crypto.AVAILABLE_CIPHER_SUITES
import dimensional.quic.tls.crypto.AVAILABLE_SIGNATURES
import dimensional.quic.tls.extension.*
import dimensional.quic.tls.handshake.TlsClientHelloHandshake
import io.ktor.network.selector.*
import io.ktor.network.sockets.*

public data class TlsClientEngineResources(
    val serverName: String,
    val extensions: List<TlsExtension> = emptyList(),
    val namedGroup: TlsNamedGroup = TlsClientEngine.DEFAULT_NAMED_GROUP
) {
    init {
        require (namedGroup in AVAILABLE_NAMED_GROUPS) {
            "The specified named group, $namedGroup, is not available. The available named groups are:\n" +
                AVAILABLE_NAMED_GROUPS.joinToString("\n") { "\t- $it" }
        }
    }
}

public class TlsClientEngine(public val resources: TlsClientEngineResources) : TlsEngine() {
    public companion object {
        /** Default TLS client extensions to send. */
        public val DEFAULT_EXTENSIONS: List<TlsExtension> = listOf(
            SupportedVersionsExtension(listOf(TlsVersion.TLS13)),
            SupportedGroupsExtension(AVAILABLE_NAMED_GROUPS),
            SignatureAlgorithmsExtension(AVAILABLE_SIGNATURES)
        )

        /** The default named group to use. */
        public val DEFAULT_NAMED_GROUP: TlsNamedGroup =
            TlsNamedGroup.secp256r1 // secp256r1 is the default since it's mandatory
    }

    private lateinit var sentClientHello: TlsClientHelloHandshake
    private lateinit var keyPair: KeyPair

    private val serverName: ServerName = ServerName(resources.serverName)

    private fun startHandshake() {
        keyPair = generateKeyPair()
    }

    private fun generateClientHello(): TlsClientHelloHandshake {
        val random = SecureRandom.nextBytes(32)

        /** Create a list of extensions to use. */
        val extensions = mutableListOf(
            ServerNameExtension(listOf(serverName))
        )

        // create the handshake message.
        return TlsClientHelloHandshake(
            random,
            AVAILABLE_CIPHER_SUITES,
            /* create the extension list. */
            TlsExtensionList(extensions + DEFAULT_EXTENSIONS + resources.extensions)
        )
    }

    private fun generateKeyPair(): KeyPair {
        val kpg = createKeyPairGenerator(resources.namedGroup)
        return kpg.generate()
    }
}
