package com.osipkat.fitnessapp.network

import com.osipkat.fitnessapp.model.Workout
import retrofit2.http.GET

interface FitnessApiService {

    @GET("get_workouts")
    suspend fun getWorkouts(): List<Workout>
}