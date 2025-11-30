package model

class Board(val size: Int = 10) {
    private val ships = mutableListOf<Ship>()
    private val takenCoordinates = mutableSetOf<Coordinate>()
    private val shots = mutableMapOf<Coordinate, ShotResult>()
    private var hits = 0

    /**
     * Tries to place a ship on the board.
     * @return true if placement was successful, false otherwise.
     */
    fun placeShip(ship: Ship): Boolean {
        if (!canPlaceShip(ship)) return false

        ships.add(ship)
        takenCoordinates.addAll(ship.coordinates)
        return true
    }

    private fun canPlaceShip(ship: Ship): Boolean {
        val shipCoords = ship.coordinates

        // Check boundaries
        if (shipCoords.any { !it.isValid(size) }) return false

        // Check collisions (direct and adjacent)
        for (coord in shipCoords) {
            if (isOccupiedOrAdjacent(coord)) return false
        }

        return true
    }

    /**
     * Checks if the coordinate or any of its orthogonal neighbors are occupied.
     * Rules state ships cannot touch by sides, but diagonals are allowed.
     */
    private fun isOccupiedOrAdjacent(coord: Coordinate): Boolean {
        val checks = listOf(
            coord,
            Coordinate(coord.x + 1, coord.y),
            Coordinate(coord.x - 1, coord.y),
            Coordinate(coord.x, coord.y + 1),
            Coordinate(coord.x, coord.y - 1)
        )

        return checks.any { it in takenCoordinates }
    }

    fun receiveShot(coord: Coordinate): ShotResult {
        // If coordinates are invalid or we somehow shot here before, throw an exception
        if (coord in shots) throw IllegalArgumentException("Cannot shoot at the same coordinate twice: $coord")
        if (!coord.isValid(size)) throw IllegalArgumentException("Invalid coordinate: $coord")

        val result = if (takenCoordinates.contains(coord)){
            val hitShip = ships.find { it.coordinates.contains(coord) }
            if (hitShip == null) throw IllegalArgumentException("Occupied coordinate $coord does not belong to any ship!")
            val shipCoords = hitShip.coordinates
            // Check if all other parts are already hit
            // TODO optional - optimize this by storing this info in the ship itself
            val otherHits = shipCoords.filter { it != coord && (shots[it] is ShotResult.Hit || shots[it] is ShotResult.Sunk) }

            if (otherHits.size == hitShip.size - 1) {
                hits++
                ShotResult.Sunk(hitShip.size)
            }
            else {
                hits++
                ShotResult.Hit
            }
        }
        else {
            ShotResult.Miss
        }

        shots[coord] = result
        return result
    }

    fun allShipsSunk(): Boolean {
        val totalShipsHp = ships.sumOf { it.size }
        return totalShipsHp == hits
    }
    
    fun getShips(): List<Ship> = ships.toList()
}