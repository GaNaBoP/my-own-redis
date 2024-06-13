import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap

class SnapshotManager (
    private val inMemoryStore: InMemoryStore
) {
    fun createSnapshotsDir() {
        val dataDirectory = File("./snapshots/")
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs()
        }
    }

    private fun createSnapshot() {
        val snapshot = File("./snapshots/" + (getLastSnapshot() + 1))
        val fileOutputStream = FileOutputStream(snapshot)
        for ((key, value) in inMemoryStore.getData()) {
            val string = "${key}:${value}\n"
            fileOutputStream.write(string.toByteArray())
        }
        fileOutputStream.close()
    }

    fun extractDataFromLastSnapshot() {
        val snapshot = File("./snapshots/" + getLastSnapshot())
        if (!snapshot.exists()) { return }
        val fileInputStream = FileInputStream(snapshot)
        val dataFromSnapshot = HashMap<String, Any>()

        fileInputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    dataFromSnapshot[parts[0]] = parts[1]
                }
            }
        }

        inMemoryStore.setData(dataFromSnapshot)
        fileInputStream.close()
        println(inMemoryStore.getData())
    }

    private fun getLastSnapshot(): Int {
        val directory = File("./snapshots")
        return directory.listFiles()?.size ?: 1
    }

    fun setInterval(seconds: Long = 60) {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                createSnapshot()
            }
        }
        timer.schedule(
            task,
            Duration.ofSeconds(seconds).toMillis(),
            Duration.ofSeconds(seconds).toMillis(),
        )
    }

}