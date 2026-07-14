package com.osipkat.fitnessapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: Int,
    val duration: Int,
    val link: String
)