package com.osipkat.fitnessapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.osipkat.fitnessapp.R
import com.osipkat.fitnessapp.model.Workout
import com.osipkat.fitnessapp.ui.icons.search
import com.osipkat.fitnessapp.ui.theme.FitnessAppTheme

@Composable
fun HomeScreen(workoutsUiState: WorkoutsUiState,
               onFilterChanged: (WorkoutFilter) -> Unit,
               onSearchChanged: (String) -> Unit,
               closeWorkoutDetailScreen: () -> Unit,
               navigateWorkoutToDetail: (Workout) -> Unit,
               modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    when {
        workoutsUiState.isLoading -> LoadingScreen(modifier = modifier.fillMaxSize())
        workoutsUiState.isError -> ErrorScreen(modifier = modifier.fillMaxSize())
        else -> WorkoutsScreen(
            uiState = workoutsUiState,
            lazyListState = lazyListState,
            onFilterChanged = onFilterChanged,
            onSearchChanged = onSearchChanged,
            closeWorkoutDetailScreen = closeWorkoutDetailScreen,
            navigateWorkoutToDetail = navigateWorkoutToDetail
        )
    }
}

@Composable
fun WorkoutSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        label = {
            Text(stringResource(R.string.search))
        },
        leadingIcon = {
            Icon(
                search,
                contentDescription = null
            )
        },
        singleLine = true
    )
}

@Composable
fun FilterBar(
    selected: WorkoutFilter,
    onSelected: (WorkoutFilter) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        WorkoutFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == selected,
                onClick = {
                    onSelected(filter)
                },
                label = {
                    Text(stringResource(filter.label))
                }
            )
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Workout,
    modifier: Modifier = Modifier,
    navigateToDetail: (Workout) -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navigateToDetail(workout) },
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(workout.title, modifier = modifier.padding(bottom = 8.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(workout.typeStrRes))
                if (workout.duration > 0) {
                    Text(pluralStringResource(
                        R.plurals.minutes,
                        workout.duration,
                        workout.duration))
                }
            }
            workout.description?.let { Text(it) }
        }
    }
}

@Composable
fun WorkoutsScreen(
    uiState: WorkoutsUiState,
    lazyListState: LazyListState,
    onFilterChanged: (WorkoutFilter) -> Unit,
    onSearchChanged: (String) -> Unit,
    closeWorkoutDetailScreen: () -> Unit,
    navigateWorkoutToDetail: (Workout) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.selectedWorkout != null && uiState.isWorkoutDetailOpen) {
        BackHandler {
            closeWorkoutDetailScreen()
        }
        WorkoutVideoPlayer(uiState.selectedWorkout)
    } else {
        Column {
            WorkoutSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchChanged
            )
            FilterBar(
                selected = uiState.selectedFilter,
                onSelected = onFilterChanged
            )
            WorkoutsList(
                workouts = uiState.workouts,
                lazyListState = lazyListState,
                navigateToDetail = navigateWorkoutToDetail,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WorkoutsList(
    workouts: List<Workout>,
    lazyListState: LazyListState,
    navigateToDetail: (Workout) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, state = lazyListState) {
        items(items = workouts, key = { it.id }) { workout ->
            WorkoutItem(
                workout = workout
            ) { workout ->
                navigateToDetail(workout)
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ResultScreen(workouts: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = workouts)
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    FitnessAppTheme {
        ResultScreen(stringResource(R.string.placeholder_result))
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutItemPreview() {
    FitnessAppTheme {
        WorkoutItem(
            Workout(
                id = 1,
                title = "Утренняя пробежка",
                description = "Идеальная пробежка для старта дня",
                type = 1,
                duration = 30
            ),
            navigateToDetail = {}
        )
    }
}
