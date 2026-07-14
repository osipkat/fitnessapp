package com.osipkat.fitnessapp.network

import com.osipkat.fitnessapp.model.Video
import com.osipkat.fitnessapp.model.Workout
import retrofit2.http.GET
import retrofit2.http.Query

interface FitnessApiService {

    @GET("get_workouts")
    suspend fun getWorkouts(): List<Workout>

    @GET("get_video")
    suspend fun getVideo(@Query("id") id: Int): Video
}