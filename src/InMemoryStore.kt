class InMemoryStore {
    private val data = HashMap<String, Any>()

    fun getData(): HashMap<String, Any> {
        return data
    }

    fun setData(data: HashMap<String, Any>) {
        this.data.putAll(data)
    }

    fun set(key: String, value: Any) {
        data[key] = value
    }

    fun get(key: String): Any? {
        return data[key]
    }

    fun remove(key: String) {
        data.remove(key)
    }
}