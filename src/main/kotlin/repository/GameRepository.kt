package repository

import game.Game
import org.springframework.stereotype.Repository

@Repository
class GameRepository internal constructor() {
    private val games = mutableMapOf<String, Game>()

    fun save(game: Game): Game {
        games[game.id] = game
        return game
    }

    fun findById(id: String): Game? {
        return games[id]
    }

    fun existsById(id: String): Boolean {
        return games.containsKey(id)
    }

    fun deleteById(id: String): Boolean {
        return games.remove(id) != null
    }

    fun findAll(): List<Game> {
        return games.values.toList()
    }

    fun count(): Int {
        return games.size
    }
}
