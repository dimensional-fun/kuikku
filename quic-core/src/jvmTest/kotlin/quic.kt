import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import mixtape.oss.kyuso.Kyuso
import mixtape.oss.kyuso.task.Task

val addr  = InetSocketAddress("www.youtube.com", 443)
val kyuso = Kyuso(Dispatchers.IO)

suspend fun main() {

    val socket = aSocket(SelectorManager(kyuso.scope.coroutineContext))
        .udp()
        .bind()

    /* create the packet receiver and start it. */
    val receiver = QuicReceiver(socket)
    receiver.start()

}

class QuicTransmitter(val wsock: DatagramWriteChannel) {

}

class QuicReceiver(val rsock: DatagramReadChannel) {
    private var task: Task? = null

    val packets: Channel<Packet> = Channel()

    fun start() {
        task = kyuso.dispatch(::loop)
    }

    fun stop(): Boolean {
        return task?.cancel() != null
    }

    private suspend fun loop() = coroutineScope {
        var count = 0
        while (isActive) {
            val datagram = rsock.receive()
            packets += Packet(datagram, Clock.System.now(), count++)
        }
    }
}

private suspend operator fun <E> SendChannel<E>.plusAssign(element: E) {
    send(element)
}

data class Packet(
    val datagram: Datagram,
    val timestamp: Instant,
    val id: Int
)
