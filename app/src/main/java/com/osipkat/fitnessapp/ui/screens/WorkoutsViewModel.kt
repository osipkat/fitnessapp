package com.osipkat.fitnessapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.osipkat.fitnessapp.FitnessApplication
import com.osipkat.fitnessapp.R
import com.osipkat.fitnessapp.data.WorkoutsRepository
import com.osipkat.fitnessapp.model.Video
import com.osipkat.fitnessapp.model.Workout
import kotlinx.coroutines.launch
import java.io.IOException

data class WorkoutsUiState (
    val workouts: List<Workout> = emptyList(),
    val selectedWorkout: Workout? = null,
    val video: Video? = null,
    val isWorkoutDetailOpen: Boolean = false,
    val selectedFilter: WorkoutFilter = WorkoutFilter.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

enum class WorkoutFilter(val type: Int, val label: Int) {
    ALL(0, R.string.workout_type_all),
    TRAINING(1, R.string.workout_type_training),
    STREAM(2, R.string.workout_type_stream),
    COMPLEX(3, R.string.workout_type_complex)
}

class WorkoutsViewModel(private val workoutsRepository: WorkoutsRepository) : ViewModel() {

    private var allWorkouts = listOf<Workout>()

    var uiState by mutableStateOf(WorkoutsUiState(isLoading = true))
        private set

    init {
        getWorkouts()
    }

    private fun getWorkouts() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                isError = false
            )
            try {
                allWorkouts = workoutsRepository.getWorkouts()
                uiState = uiState.copy(
                    isLoading = false,
                    selectedWorkout = allWorkouts.first()
                )
                updateVisibleWorkouts()
            } catch (e: IOException) {
                uiState = uiState.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    fun setSelectedWorkout(workout: Workout) {
        uiState = uiState.copy(
            selectedWorkout = workout,
            isWorkoutDetailOpen = true
        )
        viewModelScope.launch {
            try {
                val video = workoutsRepository.getVideo(workout.id)
                uiState = uiState.copy(
                    video = video
                )
            } catch (e: IOException) {

            }
        }
    }

    fun closeWorkoutDetailScreen() {
        uiState = uiState.copy(
            selectedWorkout = allWorkouts.first(),
            isWorkoutDetailOpen = false,
            video = null
        )
    }

    fun onFilterChanged(filter: WorkoutFilter) {
        uiState = uiState.copy(
            selectedFilter = filter
        )
        updateVisibleWorkouts()
    }

    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(
            searchQuery = query
        )
        updateVisibleWorkouts()
    }

    private fun updateVisibleWorkouts() {
        val visibleWorkouts = allWorkouts
            .filter { workout ->
                uiState.selectedFilter == WorkoutFilter.ALL ||
                        workout.type == uiState.selectedFilter.type
            }
            .filter { workout ->
                workout.title.contains(
                    uiState.searchQuery,
                    ignoreCase = true
                )
            }
        uiState = uiState.copy(
            workouts = visibleWorkouts
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FitnessApplication)
                val workoutsRepository = application.container.workoutsRepository
                WorkoutsViewModel(workoutsRepository = workoutsRepository)
            }
        }
    }
}