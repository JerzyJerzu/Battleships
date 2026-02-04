package strategy

import model.Board
import model.Coordinate
import model.Direction
import model.Ship
import model.ShotResult
import kotlin.random.Random

/**
 * A Random player that:
 * 1. Places ships randomly.
 * 2. Shoots randomly but remembers history.
 * 3. Respects the rule "do not shoot known empty fields" by marking neighbors of sunk ships as forbidden.
 */
open class RandomPlayerStrategy : PlayerStrategy {
    protected val shotHistory = mutableMapOf<Coordinate, ShotResult>()
    protected val forbiddenMoves = mutableSetOf<Coordinate>()

    override fun placeShips(board: Board, shipSizes: List<Int>) {
        for (size in shipSizes.sortedDescending()) {
            var placed = false
            var attempts = 0
            // Simple retry mechanism for placement
            while (!placed && attempts < 10000) {
                val direction = if (Random.nextBoolean()) Direction.HORIZONTAL else Direction.VERTICAL
                val x: Int
                val y: Int
                if (direction == Direction.HORIZONTAL) {
                    x = Random.nextInt(board.size - size + 1)
                    y = Random.nextInt(board.size)
                } else {
                    x = Random.nextInt(board.size)
                    y = Random.nextInt(board.size - size + 1)
                }
                    val ship = Ship(size, Coordinate(x, y), direction)

                if (board.placeShip(ship)) {
                    placed = true
                }
                attempts++
            }
            require(placed) { "Could not place ship of size $size after $attempts attempts" }
        }
    }

    override fun nextShot(boardSize: Int): Coordinate {
        var coord: Coordinate
        // Generate coordinates until we find one that hasn't been shot at or marked as forbidden
        do {
            val x = Random.nextInt(boardSize)
            val y = Random.nextInt(boardSize)
            coord = Coordinate(x, y)
        } while (coord in shotHistory || coord in forbiddenMoves)
        return coord
    }

    override fun recordShotResult(coord: Coordinate, result: ShotResult) {
        shotHistory[coord] = result
        if (result is ShotResult.Sunk) {
            markNeighborsAsForbidden(coord)
        }
    }

    /**
     * When a ship is sunk, we know that all surrounding orthogonal fields must be empty
     * (because ships cannot touch by sides). We mark them as forbidden to save turns.
     */
    private fun markNeighborsAsForbidden(sunkCoord: Coordinate) {
        val shipParts = mutableSetOf<Coordinate>()
        val queue = ArrayDeque<Coordinate>()

        // BFS to find the connected "Hit" components that form this sunk ship
        queue.add(sunkCoord)
        shipParts.add(sunkCoord)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            getOrthogonalNeighbors(current).forEach { n ->
                // Traverse only through Hits or the Sunk spot that belong to this ship
                if (n !in shipParts && (shotHistory[n] is ShotResult.Hit || shotHistory[n] is ShotResult.Sunk)) {
                    shipParts.add(n)
                    queue.add(n)
                }
            }
        }

        // Mark valid neighbors of the whole ship as forbidden
        shipParts.forEach { part ->
            getOrthogonalNeighbors(part).forEach { n ->
                if (n !in shotHistory) {
                    forbiddenMoves.add(n)
                }
            }
        }
    }

    private fun getOrthogonalNeighbors(c: Coordinate): List<Coordinate> {
        return listOf(
            Coordinate(c.x + 1, c.y),
            Coordinate(c.x - 1, c.y),
            Coordinate(c.x, c.y + 1),
            Coordinate(c.x, c.y - 1)
        )
    }
}