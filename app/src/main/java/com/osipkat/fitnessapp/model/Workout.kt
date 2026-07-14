package com.osipkat.fitnessapp.model

import com.osipkat.fitnessapp.R
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Workout(
    val id: Int,
    val title: String,
    val description: String? = "",
    val type: Int,
    @Serializable(with = DurationSerializer::class)
    val duration: Int
) {
    val typeStrRes: Int =
        when(type) {
            1 -> R.string.workout_type_training
            2 -> R.string.workout_type_stream
            3 -> R.string.workout_type_complex
            else -> R.string.workout_type_unknown
        }

}

object DurationSerializer : KSerializer<Int> {
    override val descriptor =
        PrimitiveSerialDescriptor("Duration", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Int {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("Can only be used with JSON")

        val element = jsonDecoder.decodeJsonElement()
        if (element is JsonNull) return 0
        return element.jsonPrimitive.intOrNull ?: 0
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeInt(value)
    }
}