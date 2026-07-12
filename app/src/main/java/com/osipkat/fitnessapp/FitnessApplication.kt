package com.osipkat.fitnessapp

import android.app.Application
import com.osipkat.fitnessapp.data.AppContainer
import com.osipkat.fitnessapp.data.DefaultAppContainer

class FitnessApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}