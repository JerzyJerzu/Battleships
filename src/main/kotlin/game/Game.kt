package game

import model.Board
import strategy.PlayerStrategy
import util.GameLogger

object Game {

    private val shipConfiguration = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)

    fun play(player1Strategy: PlayerStrategy, player2Strategy: PlayerStrategy) {
        // State must be local to the play session to ensure reusability of the Singleton
        val board1 = Board() // Player 1's board (Our board)
        val board2 = Board() // Player 2's board (Enemy board)
        var player1Shots = 0
        var player2Shots = 0

        setupPhase(board1, board2, player1Strategy, player2Strategy)

        var turn = 1
        while (!board1.allShipsSunk() && !board2.allShipsSunk()) {
            if (turn == 1) {
                player1Shots += playTurn1(board2, player1Strategy)
                turn = 2
            } else {
                player2Shots += playTurn2(board1, player2Strategy)
                turn = 1
            }
        }

        endGamePhase(board2, player1Shots, player2Shots)
    }

    private fun setupPhase(board1: Board, board2: Board, p1Strategy: PlayerStrategy, p2Strategy: PlayerStrategy) {
        p1Strategy.placeShips(board1, shipConfiguration)
        board1.getShips().forEach { ship ->
            GameLogger.logPlaceShip(ship.size, ship.head, ship.direction)
        }
        p2Strategy.placeShips(board2, shipConfiguration)
    }

    private fun playTurn1(targetBoard: Board, strategy: PlayerStrategy): Int {
        val target = strategy.nextShot(targetBoard.size)
        val result = targetBoard.receiveShot(target)
        strategy.recordShotResult(target, result)
        GameLogger.logShot(target, result)
        return 1
    }

    private fun playTurn2(targetBoard: Board, strategy: PlayerStrategy): Int {
        val target = strategy.nextShot(targetBoard.size)
        val result = targetBoard.receiveShot(target)
        strategy.recordShotResult(target, result)
        GameLogger.logEnemyShot(target, result)
        return 1
    }

    private fun endGamePhase(board2: Board, p1Shots: Int, p2Shots: Int) {
        val gameResult = if (board2.allShipsSunk()) "win" else "loss"
        GameLogger.logGameOver(gameResult, p1Shots, p2Shots)
        
        board2.getShips().forEach { ship ->
            GameLogger.logEnemyShip(ship.size, ship.head, ship.direction)
        }
    }
}