package com.osipkat.fitnessapp.data

import com.osipkat.fitnessapp.network.FitnessApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {

    val workoutsRepository: WorkoutsRepository
}

public const val baseUrl = ""

class DefaultAppContainer: AppContainer {

    // TODO ignoreUnknownKeys
    private val retroJson = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : FitnessApiService by lazy {
        retrofit.create(FitnessApiService::class.java)
    }

    override val workoutsRepository: WorkoutsRepository by lazy {
        NetworkWorkoutsRepository(retrofitService)
    }
}