package battleships

import game.GameHistory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import service.GameService
import strategy.InformedRandomPlayerStrategy
import strategy.PlayerStrategy
import strategy.RandomPlayerStrategy
import strategy.TryHardPlayerStrategy

/** Valid AI strategy types */
enum class StrategyType {
    RANDOM, INFORMED, TRYHARD
}

/** REST controller for game operations */
@RestController
@RequestMapping("/api/games")
class GameController internal constructor(
    private val gameService: GameService
) {
    // === Requests ===

    data class AIvsAIRequest(
        val player1Strategy: StrategyType = StrategyType.RANDOM,
        val player2Strategy: StrategyType = StrategyType.INFORMED
    )

    // === Response (universal) ===

    data class GameResponse(
        val gameId: String,
        val history: GameHistory
    )

    data class ErrorResponse(val error: String)

    // === Endpoints ===

    /** GET /api/games - List all saved games (summary only) */
    @GetMapping
    fun listGames(): ResponseEntity<List<GameService.GameState>> {
        return ResponseEntity.ok(gameService.listGames())
    }

    /** GET /api/games/{id} - Get full game history by ID */
    @GetMapping("/{id}")
    fun getGame(@PathVariable id: String): ResponseEntity<*> {
        return when (val result = gameService.getGameHistory(id)) {
            is GameService.GetGameHistoryResult.Success ->
                ResponseEntity.ok(GameResponse(result.gameId, result.history))

            is GameService.GetGameHistoryResult.NotFound ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse("Game ${result.gameId} not found"))
        }
    }

    /** POST /api/games/ai-vs-ai - Run AI vs AI game, return full history */
    @PostMapping("/ai-vs-ai")
    fun aiVsAi(@RequestBody request: AIvsAIRequest): ResponseEntity<*> {
        val player1Strategy = request.player1Strategy.toPlayerStrategy()
        val player2Strategy = request.player2Strategy.toPlayerStrategy()

        return when (val result = gameService.simulateGame(player1Strategy, player2Strategy)) {
            is GameService.SimulateResult.Success ->
                ResponseEntity.ok(GameResponse(result.gameId, result.history))

            is GameService.SimulateResult.Error ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse(result.message))
        }
    }

    // === Helpers ===

    private fun StrategyType.toPlayerStrategy(): PlayerStrategy = when (this) {
        StrategyType.RANDOM -> RandomPlayerStrategy()
        StrategyType.INFORMED -> InformedRandomPlayerStrategy()
        StrategyType.TRYHARD -> TryHardPlayerStrategy()
    }
}
