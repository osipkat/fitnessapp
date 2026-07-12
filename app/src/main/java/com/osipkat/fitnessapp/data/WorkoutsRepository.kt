package com.osipkat.fitnessapp.data

import com.osipkat.fitnessapp.model.Workout
import com.osipkat.fitnessapp.network.FitnessApiService

interface WorkoutsRepository {
    suspend fun getWorkouts(): List<Workout>
}

class NetworkWorkoutsRepository(
    private val fitnessApiService: FitnessApiService
): WorkoutsRepository {
    override suspend fun getWorkouts(): List<Workout> = fitnessApiService.getWorkouts()
}