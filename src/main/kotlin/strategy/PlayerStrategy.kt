package strategy

import model.Board
import model.Coordinate
import model.ShotResult

interface PlayerStrategy {
    fun placeShips(board: Board, shipSizes: List<Int>)
    fun nextShot(boardSize: Int): Coordinate
    fun recordShotResult(coord: Coordinate, result: ShotResult)
}