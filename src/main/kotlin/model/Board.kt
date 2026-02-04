package model

class Board(val size: Int = 10, shipTypes: List<Int>) {
    private val allowedShips = shipTypes.groupingBy { it }.eachCount() // Map of ship size to allowed count
    private val ships = mutableListOf<Ship>()
    private val takenCoordinates = mutableSetOf<Coordinate>()
    private val shots = mutableMapOf<Coordinate, ShotResult>()

    /**
     * Tries to place a ship on the board.
     * @return true if placement was successful, false otherwise.
     */
    fun placeShip(ship: Ship): Boolean {
        if (!canPlaceShip(ship)) return false

        // Check if we have already placed the maximum number of ships of this size
        val currentCount = ships.count { it.size == ship.size }
        val maxCount = allowedShips[ship.size] ?: 0
        if (currentCount >= maxCount) return false

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
            val hitShip = requireNotNull(ships.find { it.coordinates.contains(coord) }) {
                "Occupied coordinate $coord does not belong to any ship!"
            }
            hitShip.registerHit(coord)

            if (hitShip.isSunk) {
                ShotResult.Sunk(hitShip.size)
            }
            else {
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
        return ships.isNotEmpty() && ships.all { it.isSunk }
    }

    fun getShips(): List<Ship> = ships.toList()
}