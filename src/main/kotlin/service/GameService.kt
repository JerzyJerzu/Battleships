package service

import game.Game
import game.GameHistory
import game.GameStatus
import game.PlayerType
import game.TurnResult
import model.Coordinate
import org.springframework.stereotype.Service
import repository.GameRepository
import strategy.PlayerStrategy

/** Service layer for game operations. Returns sealed result types. */
@Service
class GameService internal constructor(
    private val gameRepository: GameRepository
) {
    // Result types
    sealed class CreateGameResult {
        data class Success(val gameId: String) : CreateGameResult()
        data class Error(val message: String) : CreateGameResult()
    }

    sealed class GetGameResult {
        data class Success(val game: GameState) : GetGameResult()
        data class NotFound(val gameId: String) : GetGameResult()
    }

    sealed class GetGameHistoryResult {
        data class Success(val gameId: String, val history: GameHistory) : GetGameHistoryResult()
        data class NotFound(val gameId: String) : GetGameHistoryResult()
    }

    sealed class MoveResult {
        data class Success(val turnResult: TurnResult) : MoveResult()
        data class NotFound(val gameId: String) : MoveResult()
        data class GameAlreadyFinished(val gameId: String) : MoveResult()
        data class InvalidMove(val message: String) : MoveResult()
        data class NotYourTurn(val currentPlayer: Int) : MoveResult()
    }

    sealed class SimulateResult {
        data class Success(val gameId: String, val winner: Int, val history: GameHistory) : SimulateResult()
        data class Error(val message: String) : SimulateResult()
    }

    // DTO
    data class GameState(
        val id: String,
        val status: GameStatus,
        val currentPlayer: Int,
        val currentPlayerType: PlayerType,
        val winner: Int?,
        val currentTurn: Int
    )

    /** Creates a new game and stores it in repository */
    fun createGame(
        player1Strategy: PlayerStrategy,
        player2Strategy: PlayerStrategy,
        player1Type: PlayerType = PlayerType.AI,
        player2Type: PlayerType = PlayerType.AI
    ): CreateGameResult {
        return try {
            val game = Game(
                player1Strategy = player1Strategy,
                player2Strategy = player2Strategy,
                player1Type = player1Type,
                player2Type = player2Type
            )
            gameRepository.save(game)
            CreateGameResult.Success(game.id)
        } catch (e: Exception) {
            CreateGameResult.Error("Failed to create game: ${e.message}")
        }
    }

    /** Retrieves current game state by ID */
    fun getGame(gameId: String): GetGameResult {
        val game = gameRepository.findById(gameId)
            ?: return GetGameResult.NotFound(gameId)

        return GetGameResult.Success(game.toGameState())
    }

    /** Retrieves full game history by ID */
    fun getGameHistory(gameId: String): GetGameHistoryResult {
        val game = gameRepository.findById(gameId)
            ?: return GetGameHistoryResult.NotFound(gameId)

        return GetGameHistoryResult.Success(gameId, game.history)
    }

    /** Lists all saved games (summary only, no history) */
    fun listGames(): List<GameState> {
        return gameRepository.findAll().map { it.toGameState() }
    }

    /** Executes a move for HUMAN player at given coordinate */
    fun playHumanMove(gameId: String, coordinate: Coordinate): MoveResult {
        val game = gameRepository.findById(gameId)
            ?: return MoveResult.NotFound(gameId)

        if (game.status == GameStatus.FINISHED) {
            return MoveResult.GameAlreadyFinished(gameId)
        }

        if (game.getCurrentPlayerType() != PlayerType.HUMAN) {
            return MoveResult.NotYourTurn(game.currentPlayer)
        }

        return try {
            val turnResult = game.playNextTurn(coordinate)
            if (turnResult != null) {
                MoveResult.Success(turnResult)
            } else {
                MoveResult.GameAlreadyFinished(gameId)
            }
        } catch (e: IllegalArgumentException) {
            MoveResult.InvalidMove(e.message ?: "Invalid move")
        }
    }

    /** Executes a move for AI player (strategy decides coordinate) */
    fun playAiTurn(gameId: String): MoveResult {
        val game = gameRepository.findById(gameId)
            ?: return MoveResult.NotFound(gameId)

        if (game.status == GameStatus.FINISHED) {
            return MoveResult.GameAlreadyFinished(gameId)
        }

        if (game.getCurrentPlayerType() != PlayerType.AI) {
            return MoveResult.NotYourTurn(game.currentPlayer)
        }

        return try {
            val turnResult = game.playNextTurn()
            if (turnResult != null) {
                gameRepository.save(game)  // Save updated game state
                MoveResult.Success(turnResult)
            } else {
                MoveResult.GameAlreadyFinished(gameId)
            }
        } catch (e: Exception) {
            MoveResult.InvalidMove(e.message ?: "AI move failed")
        }
    }

    /** Runs complete AI vs AI game, returns full history */
    fun simulateGame(
        player1Strategy: PlayerStrategy,
        player2Strategy: PlayerStrategy
    ): SimulateResult {
        return try {
            val game = Game(
                player1Strategy = player1Strategy,
                player2Strategy = player2Strategy,
                player1Type = PlayerType.AI,
                player2Type = PlayerType.AI
            )

            // Run game to completion
            while (game.status == GameStatus.IN_PROGRESS) {
                game.playNextTurn()
            }

            // Save completed game to repository
            gameRepository.save(game)

            val winner = requireNotNull(game.winner) { "Game finished but no winner" }
            SimulateResult.Success(game.id, winner, game.history)
        } catch (e: Exception) {
            SimulateResult.Error("Simulation failed: ${e.message}")
        }
    }

    /** Converts Game entity to GameState DTO */
    private fun Game.toGameState(): GameState {
        return GameState(
            id = this.id,
            status = this.status,
            currentPlayer = this.currentPlayer,
            currentPlayerType = this.getCurrentPlayerType(),
            winner = this.winner,
            currentTurn = this.currentTurn
        )
    }
}
