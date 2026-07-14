package com.osipkat.fitnessapp.data

import com.osipkat.fitnessapp.model.Video
import com.osipkat.fitnessapp.model.Workout
import com.osipkat.fitnessapp.network.FitnessApiService

interface WorkoutsRepository {
    suspend fun getWorkouts(): List<Workout>
    suspend fun getVideo(id: Int): Video
}

class NetworkWorkoutsRepository(
    private val fitnessApiService: FitnessApiService
): WorkoutsRepository {
    override suspend fun getWorkouts(): List<Workout> = fitnessApiService.getWorkouts()
    override suspend fun getVideo(id: Int): Video = fitnessApiService.getVideo(id)
}