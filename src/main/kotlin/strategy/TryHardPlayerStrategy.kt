package strategy

import model.Board
import model.Coordinate
import model.ShotResult

class TryHardPlayerStrategy : InformedRandomPlayerStrategy() {
    private val remainingEnemyShips = mutableListOf<Int>()
    private var capturedBoardSize = 0
    private var minShipSize = 1

    override fun placeShips(board: Board, shipSizes: List<Int>) {
        super.placeShips(board, shipSizes)
        capturedBoardSize = board.size
        remainingEnemyShips.clear()
        remainingEnemyShips.addAll(shipSizes)

    }

    override fun recordShotResult(coord: Coordinate, result: ShotResult) {
        super.recordShotResult(coord, result)

        if (result is ShotResult.Sunk) {
            // Remove one instance of the sunk ship size
            remainingEnemyShips.remove(result.shipSize)

            markImpossibleFieldsAsForbidden()
        }
    }

    private fun markImpossibleFieldsAsForbidden() {
        if (capturedBoardSize == 0) return
        val updatedMinShipSize = remainingEnemyShips.minOrNull() ?: return
        if (updatedMinShipSize > minShipSize) {
            minShipSize = updatedMinShipSize
        }
        else {
            return
        }
        println("Excluding filed where no ship can fit minShipSize: $minShipSize")
        // Identify cells that cannot effectively hold any of the remaining ships
        // We check horizontal availability and vertical availability separately.
        // TODO Get rid of of the reptitive code later
        val uselessHorizontal = mutableSetOf<Coordinate>()
        val uselessVertical = mutableSetOf<Coordinate>()
        // TODO check it against warning "Warning:(48, 19) This range is empty. Did you mean to use 'rangeTo'?"
        // Check rows for horizontal segments
        for (y in 0 until capturedBoardSize) {
            var segmentLength = 0
            val segmentCoords = mutableListOf<Coordinate>()

            for (x in 0 until capturedBoardSize) {
                val c = Coordinate(x, y)
                if (isPassable(c)) {
                    segmentLength++
                    segmentCoords.add(c)
                }
                else {
                    // Segment ended (blocked)
                    if (segmentLength < minShipSize) {
                        uselessHorizontal.addAll(segmentCoords)
                    }
                    segmentLength = 0
                    segmentCoords.clear()
                }
            }
            // Check last segment in row
            if (segmentLength < minShipSize) {
                uselessHorizontal.addAll(segmentCoords)
            }
        }

        // Check cols for vertical segments
        for (x in 0 until capturedBoardSize) {
            var segmentLength = 0
            val segmentCoords = mutableListOf<Coordinate>()

            for (y in 0 until capturedBoardSize) {
                val c = Coordinate(x, y)
                if (isPassable(c)) {
                    segmentLength++
                    segmentCoords.add(c)
                } else {
                    // Segment ended (blocked)
                    if (segmentLength < minShipSize) {
                        uselessVertical.addAll(segmentCoords)
                    }
                    segmentLength = 0
                    segmentCoords.clear()
                }
            }
            // Check last segment in col
            if (segmentLength < minShipSize) {
                uselessVertical.addAll(segmentCoords)
            }
        }

        // A cell is forbidden if it's useless in BOTH directions
        // (meaning no ship of minSize can cover it horizontally AND no ship of minSize can cover it vertically)
        val trulyForbiddenCandidates = uselessHorizontal.intersect(uselessVertical)

        // Filter out any coordinates that have already been shot at, then add to forbiddenMoves
        trulyForbiddenCandidates.filterTo(forbiddenMoves) { it !in shotHistory }
    }

    private fun isPassable(c: Coordinate): Boolean {
        if (c in forbiddenMoves || c in shotHistory) return false
        return true
    }
}