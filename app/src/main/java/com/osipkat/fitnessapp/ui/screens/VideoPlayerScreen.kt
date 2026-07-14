package com.osipkat.fitnessapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.osipkat.fitnessapp.data.baseUrl
import com.osipkat.fitnessapp.model.Video
import com.osipkat.fitnessapp.model.Workout

@Composable
fun rememberManagedPlayer(
    url: String
): ExoPlayer {

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }

    DisposableEffect(lifecycle) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_STOP ->
                    player.pause()

                Lifecycle.Event.ON_DESTROY ->
                    player.release()

                else -> {}
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            player.release()
        }
    }

    return player
}

@Composable
fun VideoPlayer(
    url: String
) {

    val player = rememberManagedPlayer(url)

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Composable
fun WorkoutVideoPlayer(
    video: Video,
    workout: Workout
) {
    Column() {
        WorkoutItem(workout, navigateToDetail = {})
        VideoPlayer(
            url = baseUrl + video.link.drop(1)
        )
    }

}