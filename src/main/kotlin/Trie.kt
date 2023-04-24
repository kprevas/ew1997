class Trie<D> {
    private val data = mutableListOf<D>()
    private var left: Trie<D>? = null
    private var right: Trie<D>? = null

    fun insert(datum: D, path: List<Boolean>) {
        if (path.isEmpty()) {
            data.add(datum)
        } else if (path[0]) {
            if (right == null) {
                right = Trie()
            }
            right!!.insert(datum, path.subList(1, path.size))
        } else {
            if (left == null) {
                left = Trie()
            }
            left!!.insert(datum, path.subList(1, path.size))
        }
    }

    fun longestShared(): Result<D>? {
        val longestLeft = left?.longestShared()
        val longestRight = right?.longestShared()
        val leftLength = longestLeft?.length ?: -1
        val rightLength = longestRight?.length ?: -1
        val max = if (leftLength > rightLength) {
            longestLeft
        } else if (rightLength > leftLength) {
            longestRight
        } else if (leftLength > 0) {
            Result(longestLeft!!.length, longestLeft.data + longestRight!!.data)
        } else {
            null
        }
        return if (max != null) {
            Result(max.length + 1, max.data)
        } else if (data.size > 1) {
            Result(0, listOf(data))
        } else {
            null
        }
    }
}

data class Result<D>(val length: Int, val data: List<List<D>>)