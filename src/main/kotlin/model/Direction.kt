package model

enum class Direction {
    HORIZONTAL, VERTICAL;

    override fun toString(): String = name.lowercase()
}