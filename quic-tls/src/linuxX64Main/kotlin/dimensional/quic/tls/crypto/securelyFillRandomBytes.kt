package dimensional.quic.tls.crypto

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.usePinned
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread

internal actual fun fillRandomBytes(dst: ByteArray) {
    if (dst.isEmpty()) {
        return
    }

    dst.usePinned { pin ->
        val ptr = pin.addressOf(0)
        val file = fopen("/dev/urandom", "rb")
        if (file != null) {
            fread(ptr, 1.convert(), dst.size.convert(), file)
            for (n in dst.indices) dst[n] = ptr[n]
            fclose(file)
        }
    }
}
