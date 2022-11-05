import dimensional.anzen.crypto.digest.Digest
import dimensional.anzen.crypto.digest.finalize
import dimensional.anzen.crypto.hmac
import dimensional.anzen.crypto.params.KeyParameter
import io.matthewnelson.component.encoding.base16.encodeBase16
import java.security.MessageDigest
import kotlin.random.Random
import kotlin.time.measureTimedValue

val input = Random.nextBytes(256)

fun main() {
    val key = KeyParameter("secret".encodeToByteArray(), "HmacSHA256")
    println(input.hmac(key, "sha256").encodeBase16())

    sha256()
    println()
    sha384()
    println()
    sha512()
    println()
    md5()
}

fun sha256() {
    test(
        "SHA256",
        org.bouncycastle.crypto.digests.SHA256Digest(),
        MessageDigest.getInstance("SHA256"),
        Digest("SHA256", false)
    )
}

fun sha384() {
    test(
        "SHA384",
        org.bouncycastle.crypto.digests.SHA384Digest(),
        MessageDigest.getInstance("SHA384"),
        Digest("SHA384", false)
    )
}

fun sha512() {
    test(
        "SHA512",
        org.bouncycastle.crypto.digests.SHA512Digest(),
        MessageDigest.getInstance("SHA512"),
        Digest("SHA512", false)
    )
}

fun md5() {
    test(
        "MD5",
        org.bouncycastle.crypto.digests.MD5Digest(),
        MessageDigest.getInstance("MD5"),
        Digest("MD5", false)
    )
}

fun test(
    name: String,
    bcDigest: org.bouncycastle.crypto.Digest,
    jvDigest: MessageDigest,
    azDigest: Digest,
) {
    println("-- $name --")

    /* bouncycastle */
    val (bcResult, bcTook) = measureTimedValue {
        bcDigest.update(input, 0, input.size)

        val bcResult = ByteArray(bcDigest.digestSize)
        bcDigest.doFinal(bcResult, 0)
        bcResult
    }

    /* java */
    val (jvResult, jvTook) = measureTimedValue {
        jvDigest.update(input)
        jvDigest.digest()
    }

    /* anzen */
    val (azResult, azTook) = measureTimedValue {
        azDigest.update(input)
        azDigest.finalize()
    }

    // results
    println("bouncycastle: " + bcResult.encodeBase16().lowercase() + " ($bcTook)")
    println("java:         " + jvResult.encodeBase16().lowercase() + " ($jvTook)")
    println("anzen:        " + azResult.encodeBase16().lowercase() + " ($azTook)")
}
