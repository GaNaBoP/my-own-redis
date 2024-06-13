fun main() {
    val store = InMemoryStore()

    val snapshotManager = SnapshotManager(store)
    snapshotManager.createSnapshotsDir()
    snapshotManager.extractDataFromLastSnapshot()
    snapshotManager.setInterval()

    val server = Server(store)
    server.start()
}
