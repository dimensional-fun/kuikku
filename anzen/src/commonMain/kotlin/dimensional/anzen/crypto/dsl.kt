package dimensional.anzen.crypto

import dimensional.anzen.crypto.digest.digest
import dimensional.anzen.crypto.mac.Mac
import dimensional.anzen.crypto.mac.finalize
import dimensional.anzen.crypto.params.CryptoParameters

public fun ByteArray.md5(offset: Int = 0, length: Int = size): ByteArray = digest("md5", offset, length)

public fun ByteArray.sha256(offset: Int = 0, length: Int = size): ByteArray = digest("sha256", offset, length)

public fun ByteArray.sha384(offset: Int = 0, length: Int = size): ByteArray = digest("sha384", offset, length)

public fun ByteArray.sha512(offset: Int = 0, length: Int = size): ByteArray = digest("sha512", offset, length)

public fun ByteArray.hmac(
    params: CryptoParameters,
    digest: String,
    offset: Int = 0,
    length: Int = size
): ByteArray = Mac("hmac$digest")
    .initialize(params)
    .update(this, offset, length)
    .finalize()
