package com.osipkat.fitnessapp.model

import com.osipkat.fitnessapp.R
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: Int,
    val title: String,
// TODO    val description: String,
    val type: Int,
// TODO   val duration: Int
) {
    val typeStrRes: Int =
        when(type) {
            1 -> R.string.workout_type_training
            2 -> R.string.workout_type_stream
            3 -> R.string.workout_type_complex
            else -> R.string.workout_type_unknown
        }

}