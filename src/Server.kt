import java.net.ServerSocket
import java.net.Socket

class Server (
    private val store: InMemoryStore
) {
    private val serverSocket = ServerSocket(6543)

    fun start() {
        println("Server listening on port ${serverSocket.localPort}")

        while (true) {
            val clientSocket = serverSocket.accept()
            println("Client connected: ${clientSocket.remoteSocketAddress}")
            Thread{
                handleClient(clientSocket)
            }.start()
        }
    }

    private fun handleClient(client: Socket) {
        val inputStream = client.getInputStream()
        val outputStream = client.getOutputStream()

        val buffer = ByteArray(1024)
        var bytesRead = inputStream.read(buffer)
        while (bytesRead != -1) {
            val request = String(buffer, 0, bytesRead)
            val response = handleMessage(request)

            outputStream.write("$response".toByteArray())
            outputStream.flush()

            bytesRead = inputStream.read(buffer)
        }
        client.close()
    }

    private fun handleMessage(msg: String): Any? {
        if (msg.startsWith("get")) {
            return store.get(getWord(msg, 1))
        } else if (msg.startsWith("set")) {
            var value = ""
            for (i in 2..<msg.split(" ").size) {
                value += getWord(msg, i) + " "
            }
            store.set(getWord(msg, 1), value)
            return "OK!"
        } else if (msg.startsWith("remove")) {
            store.remove(getWord(msg, 1))
            return "OK!"
        }
        return "unknown command"
    }

    private fun getWord(string: String, pos: Int): String {
        return string.split(" ")[pos]
    }

}