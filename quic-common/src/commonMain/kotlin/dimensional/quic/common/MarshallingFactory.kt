package dimensional.quic.common

import io.ktor.utils.io.core.*

public interface Marshal<T : Any> {
    public fun marshal(value: T): ByteReadPacket
}

public interface Unmarshal<T : Any> {
    public fun unmarshal(payload: ByteReadPacket): T?
}

public interface MarshallingFactory<T : Any> : Marshal<T>, Unmarshal<T>
