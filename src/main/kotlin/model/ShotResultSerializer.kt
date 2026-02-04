package model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/** Serializes ShotResult to simple string for frontend: "miss" or "hit" (Sunk counts as hit, as it makes no difference for frontend) */
class ShotResultSerializer : JsonSerializer<ShotResult>() {
    override fun serialize(value: ShotResult, gen: JsonGenerator, serializers: SerializerProvider) {
        val result = when (value) {
            is ShotResult.Miss -> "miss"
            is ShotResult.Hit -> "hit"
            is ShotResult.Sunk -> "hit"
        }
        gen.writeString(result)
    }
}
