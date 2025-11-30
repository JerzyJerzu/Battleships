package strategy

import model.Coordinate
import model.ShotResult

open class InformedRandomPlayerStrategy : RandomPlayerStrategy() {
    private val hitsNotSunk = mutableListOf<Coordinate>()

    override fun nextShot(boardSize: Int): Coordinate {
        // If we are not in hunting mode, just shoot randomly
        if (hitsNotSunk.isEmpty()) {
            return super.nextShot(boardSize)
        }

        val potentialTargets = getPotentialTargets()

        // Find the first valid target that hasn't been shot at yet
        val target = potentialTargets.firstOrNull {
            it.isValid(boardSize) &&
                    it !in shotHistory &&
                    it !in forbiddenMoves
        }

        // If no smart targets are valid (should rarely happen if logic is correct), fallback to random
        return target ?: super.nextShot(boardSize)
    }

    override fun recordShotResult(coord: Coordinate, result: ShotResult) {
        super.recordShotResult(coord, result)
        when (result) {
            is ShotResult.Hit -> hitsNotSunk.add(coord)
            is ShotResult.Sunk -> hitsNotSunk.clear()
            else -> {} // Miss: do nothing, keep hunting
        }
    }

    private fun getPotentialTargets(): List<Coordinate> {
        if (hitsNotSunk.isEmpty()) return emptyList()

        // If we only have one hit, we don't know the direction yet. Try all 4 neighbors.
        if (hitsNotSunk.size == 1) {
            val hit = hitsNotSunk[0]
            return listOf(
                Coordinate(hit.x + 1, hit.y),
                Coordinate(hit.x - 1, hit.y),
                Coordinate(hit.x, hit.y + 1),
                Coordinate(hit.x, hit.y - 1)
            ).shuffled()
        }

        // If we have multiple hits, we can establish the direction (Horizontal or Vertical)
        val sortedHits = hitsNotSunk.sortedBy { it.x + it.y }
        val first = sortedHits.first()
        val last = sortedHits.last()

        return if (first.x == last.x) {
            // Vertical alignment: try above the top and below the bottom
            listOf(
                Coordinate(first.x, first.y - 1),
                Coordinate(last.x, last.y + 1)
            )
        } else {
            // Horizontal alignment: try left of the first and right of the last
            listOf(
                Coordinate(first.x - 1, first.y),
                Coordinate(last.x + 1, last.y)
            )
        }
    }
}