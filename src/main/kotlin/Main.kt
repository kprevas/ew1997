import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.time.Duration
import java.time.Instant
import kotlin.math.min

data class TeamSeason(val team: String, val season: String) {
    override fun toString() = "$season $team"
}

data class GameSpan(val teamSeason: TeamSeason, val start: Int, val length: Int) {
    override fun toString() = "$teamSeason games ${start + 1}-${start + length}"
}

val trie = Trie<GameSpan>()
val teamRecords = mutableMapOf<TeamSeason, MutableList<Boolean>>()

fun readSeason(file: File) {
    val season = file.name.substring(2..5)
    val rows = csvReader().readAll(file)
    for (row in rows) {
        val homeTeam = row[6]
        val visTeam = row[3]
        val homeWin = Integer.parseInt(row[10]) > Integer.parseInt(row[9])
        fun addGame(team: String, win: Boolean) {
            val key = TeamSeason(team, season)
            if (!teamRecords.containsKey(key)) {
                teamRecords[key] = mutableListOf()
            }
            teamRecords[key]!!.add(win)
        }
        addGame(homeTeam, homeWin)
        addGame(visTeam, !homeWin)
    }
}

fun eachSpan(minSpanLength: Int, maxSpanLength: Int, doSpan: (GameSpan, List<Boolean>) -> Unit) {
    for ((teamSeason, records) in teamRecords) {
        for (startGame in 0..records.size - minSpanLength) {
            for (spanLength in minSpanLength..(min(maxSpanLength, records.size - startGame))) {
                doSpan(
                    GameSpan(teamSeason, startGame, spanLength),
                    records.subList(startGame, startGame + spanLength)
                )
            }
        }
    }
}

fun main(args: Array<String>) {
    val start = Instant.now()
    for (file in File("gl1871_2022").listFiles()!!) {
        readSeason(file)
    }
    val minSpanLength = Integer.parseInt(args[0])
    val maxSpanLength = Integer.parseInt(args[1])

    var spanCount = 0
    eachSpan(minSpanLength, maxSpanLength) { _, _ -> spanCount++ }
    println("$spanCount total spans")

    eachSpan(minSpanLength, maxSpanLength, trie::insert)
    val longest = trie.longestShared()
    println(
        if (longest != null) {
            "Longest match: ${longest.length} games - ${longest.data}"
        } else {
            "No matching spans found between lengths $minSpanLength and $maxSpanLength"
        }
    )

    println("Total execution time ${Duration.between(start, Instant.now()).toMillis()}ms")
}