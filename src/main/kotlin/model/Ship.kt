package model

class Ship(val size: Int, val head: Coordinate, val direction: Direction) {
    val coordinates: Set<Coordinate> by lazy {
        (0 until size).map { i ->
            if (direction == Direction.HORIZONTAL) {
                Coordinate(head.x + i, head.y)
            } else {
                Coordinate(head.x, head.y + i)
            }
        }.toSet()
    }

    private val hits = mutableSetOf<Coordinate>()

    fun registerHit(coord: Coordinate) {
        if (coord in coordinates) {
            hits.add(coord)
        }
    }

    val isSunk: Boolean
        get() = hits.size == size
}