package strategy

import model.Board
import model.Coordinate
import model.ShotResult

interface PlayerStrategy {
    fun placeShips(board: Board, shipSizes: List<Int>)
    fun nextShot(boardSize: Int): Coordinate
    fun recordShotResult(coord: Coordinate, result: ShotResult)

    /** Returns coordinates this strategy won't shoot (knows they're empty) */
    fun getForbiddenMoves(): Set<Coordinate>

    /** Returns all shots made and their results */
    fun getShotHistory(): Map<Coordinate, ShotResult>
}