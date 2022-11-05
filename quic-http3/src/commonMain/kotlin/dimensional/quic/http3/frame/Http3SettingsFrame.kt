package dimensional.quic.http3.frame

import dimensional.quic.http3.Http3Connection
import dimensional.quic.common.Unmarshal
import dimensional.quic.common.readVarInt
import dimensional.quic.common.writeVarInt
import io.ktor.utils.io.core.*

public data class Http3SettingsFrame(
    val settings: List<Setting> = emptyList()
) : Http3Frame(Http3FrameType.Settings) {
    public companion object : Unmarshal<Http3Frame> {
        public override fun unmarshal(payload: ByteReadPacket): Http3SettingsFrame {
            val settings = mutableListOf<Setting>()
            while (payload.isNotEmpty) {
                val identifier = payload.readVarInt() ?: break
                val value      = payload.readVarInt() ?: break
                settings += Setting(identifier, value)
            }

            return Http3SettingsFrame(settings)
        }
    }

    override suspend fun marshalPayload(connection: Http3Connection): ByteReadPacket = buildPacket {
        for (setting in settings) {
            writeVarInt(setting.identifier)
            writeVarInt(setting.value)
        }
    }

    public data class Setting(val identifier: Long, val value: Long)
}
