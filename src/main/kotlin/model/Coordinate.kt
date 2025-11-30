package model

data class Coordinate(val x: Int, val y: Int) {
    override fun toString(): String = "($x,$y)"

    fun isValid(boardSize: Int): Boolean {
        return x in 0 until boardSize && y in 0 until boardSize
    }
}
