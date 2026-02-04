package model

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ShotResultSerializer::class)
sealed class ShotResult {
    object Miss : ShotResult() {
        override fun toString() = "miss"
    }
    object Hit : ShotResult() {
        override fun toString() = "hit"
    }
    data class Sunk(val shipSize: Int) : ShotResult() {
        override fun toString() = "sunk ship-size=$shipSize"
    }
}