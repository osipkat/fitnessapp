package com.osipkat.fitnessapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.osipkat.fitnessapp.R
import com.osipkat.fitnessapp.ui.screens.HomeScreen
import com.osipkat.fitnessapp.ui.screens.WorkoutsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { FitnessTopAppBar(scrollBehavior = scrollBehavior) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            val workoutsViewModel: WorkoutsViewModel = viewModel(factory = WorkoutsViewModel.Factory)
            HomeScreen(
                workoutsUiState = workoutsViewModel.uiState,
                onFilterChanged = workoutsViewModel::onFilterChanged,
                onSearchChanged = workoutsViewModel::onSearchQueryChanged,
                closeWorkoutDetailScreen = workoutsViewModel::closeWorkoutDetailScreen,
                navigateWorkoutToDetail = { workout ->
                    workoutsViewModel.setSelectedWorkout(workout)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}
