package dimensional.quic.http3

import dimensional.quic.http3.qpack.QPackDecoder
import dimensional.quic.http3.qpack.QPackEncoder

public class Http3Connection {
    /** The QPack decoder & encode instances */
    public val qpack: QPack = QPack(
        QPackEncoder(),
        QPackDecoder()
    )

    public data class QPack(val encoder: QPackEncoder, val decoder: QPackDecoder)
}
