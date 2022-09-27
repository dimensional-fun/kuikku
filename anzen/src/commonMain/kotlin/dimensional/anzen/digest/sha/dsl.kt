package dimensional.anzen.digest.sha

import dimensional.anzen.digest.finalize

public fun ByteArray.sha256(offset: Int = 0, length: Int = size): ByteArray = SHA256()
    .update(this, offset, length)
    .finalize()

public fun ByteArray.sha384(offset: Int = 0, length: Int = size): ByteArray = SHA384()
    .update(this, offset, length)
    .finalize()

public fun ByteArray.sha512(offset: Int = 0, length: Int = size): ByteArray = SHA512()
    .update(this, offset, length)
    .finalize()
