package game

import model.Board
import model.Coordinate
import model.ShotResult
import strategy.PlayerStrategy
import util.GameLogger
import java.util.UUID

enum class GameStatus { IN_PROGRESS, FINISHED }

enum class PlayerType { HUMAN, AI }

data class TurnResult(
    val player: Int,
    val target: Coordinate,
    val result: ShotResult,
    val gameOver: Boolean,
    val winner: Int?
)

class Game(
    val id: String = UUID.randomUUID().toString(),
    private val player1Strategy: PlayerStrategy,
    private val player2Strategy: PlayerStrategy,
    private val player1Type: PlayerType = PlayerType.AI,
    private val player2Type: PlayerType = PlayerType.AI,
    private val shipConfiguration: List<Int> = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
) {
    // Game state - instance properties
    val board1 = Board(shipTypes = shipConfiguration)  // Player 1's board
    val board2 = Board(shipTypes = shipConfiguration)  // Player 2's board

    var status: GameStatus = GameStatus.IN_PROGRESS
        private set
    var currentTurn: Int = 0
        private set
    var currentPlayer: Int = 1
        private set
    var winner: Int? = null
        private set

    private var player1Shots = 0
    private var player2Shots = 0

    init {
        // Setup happens on creation - ships are placed automatically
        setupPhase()
    }

    /**
     * Execute one turn of the game.
     * For HUMAN players: move must be provided
     * For AI players: move must NOT be provided (strategy decides)
     */
    fun playNextTurn(move: Coordinate? = null): TurnResult? {
        if (status == GameStatus.FINISHED) return null

        val (targetBoard, strategy, playerType) = if (currentPlayer == 1) {
            Triple(board2, player1Strategy, player1Type)
        } else {
            Triple(board1, player2Strategy, player2Type)
        }

        // Explicit validation based on player type
        val target = when (playerType) {
            PlayerType.HUMAN -> {
                move ?: throw IllegalArgumentException(
                    "Human player (player $currentPlayer) must provide a move"
                )
            }
            PlayerType.AI -> {
                if (move != null) {
                    throw IllegalArgumentException(
                        "AI player (player $currentPlayer) chooses its own move - do not provide a move"
                    )
                }
                strategy.nextShot(targetBoard.size)
            }
        }

        // Execute the shot
        val result = targetBoard.receiveShot(target)
        strategy.recordShotResult(target, result)

        // Update counters
        if (currentPlayer == 1) player1Shots++ else player2Shots++
        currentTurn++

        val shootingPlayer = currentPlayer

        // Check for winner
        if (targetBoard.allShipsSunk()) {
            status = GameStatus.FINISHED
            winner = currentPlayer
        }

        // Switch player for next turn
        currentPlayer = if (currentPlayer == 1) 2 else 1

        return TurnResult(
            player = shootingPlayer,
            target = target,
            result = result,
            gameOver = status == GameStatus.FINISHED,
            winner = winner
        )
    }

    /**
     * Play the entire game in a loop (for CLI usage).
     * Only works for AI vs AI games.
     */
    fun play() {
        if (player1Type == PlayerType.HUMAN || player2Type == PlayerType.HUMAN) {
            throw IllegalStateException(
                "play() only works for AI vs AI games. Use playNextTurn() for games with human players."
            )
        }

        while (status == GameStatus.IN_PROGRESS) {
            val turn = playNextTurn()!!

            // Log based on who shot
            if (turn.player == 1) {
                GameLogger.logShot(turn.target, turn.result)
            } else {
                GameLogger.logEnemyShot(turn.target, turn.result)
            }
        }

        endGamePhase()
    }

    private fun setupPhase() {
        player1Strategy.placeShips(board1, shipConfiguration)
        board1.getShips().forEach { ship ->
            GameLogger.logPlaceShip(ship.size, ship.head, ship.direction)
        }
        player2Strategy.placeShips(board2, shipConfiguration)
    }

    private fun endGamePhase() {
        val gameResult = if (board2.allShipsSunk()) "win" else "loss"
        GameLogger.logGameOver(gameResult, player1Shots, player2Shots)

        board2.getShips().forEach { ship ->
            GameLogger.logEnemyShip(ship.size, ship.head, ship.direction)
        }
    }

    // Helper function to get current player's type (useful for frontend)
    fun getCurrentPlayerType(): PlayerType {
        return if (currentPlayer == 1) player1Type else player2Type
    }

    // Helper to get strategy info for display
    fun getPlayer1StrategyName(): String = player1Strategy::class.simpleName ?: "Unknown"
    fun getPlayer2StrategyName(): String = player2Strategy::class.simpleName ?: "Unknown"
}
