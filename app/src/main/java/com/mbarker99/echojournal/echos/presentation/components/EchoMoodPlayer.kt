package com.mbarker99.echojournal.echos.presentation.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.mbarker99.echojournal.core.presentation.designsystem.theme.MoodPrimary25
import com.mbarker99.echojournal.core.presentation.designsystem.theme.MoodPrimary35
import com.mbarker99.echojournal.core.presentation.designsystem.theme.MoodPrimary80
import com.mbarker99.echojournal.core.presentation.util.formatMMSS
import com.mbarker99.echojournal.echos.presentation.echos.model.PlaybackState
import com.mbarker99.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun EchoMoodPlayer(
    moodUi: MoodUi?,
    playbackState: PlaybackState,
    playerProgress: () -> Float,
    durationPlayed: Duration,
    totalPlaybackDuration: Duration,
    amplitudes: List<Float>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier,
    amplitudeBarWidth: Dp = 5.dp,
    amplitudeBarSpacing: Dp = 4.dp,

    ) {
    val iconTint = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val trackFillColor = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val backgroundColor = when (moodUi) {
        null -> MoodPrimary25
        else -> moodUi.colorSet.faded
    }
    val trackColor = when (moodUi) {
        null -> MoodPrimary35
        else -> moodUi.colorSet.desaturated
    }

    val formattedDurationText = remember(durationPlayed, totalPlaybackDuration) {
        "${durationPlayed.formatMMSS()}/${totalPlaybackDuration.formatMMSS()}"
    }

    val density = LocalDensity.current

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EchoPlaybackButton(
                playbackState = playbackState,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = iconTint
                )
            )

            EchoPlayBar(
                amplitudeBarWidth = amplitudeBarWidth,
                amplitudeBarSpacing = amplitudeBarSpacing,
                amplitudes = amplitudes,
                trackColor = trackColor,
                trackFillColor = trackFillColor,
                playerProgress = playerProgress,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        vertical = 10.dp,
                        horizontal = 8.dp
                    )
                    .fillMaxHeight()
                    .onSizeChanged {
                        if (it.width > 0) {
                            onTrackSizeAvailable(
                                TrackSizeInfo(
                                    trackWidth = it.width.toFloat(),
                                    barWidth = with(density) { amplitudeBarWidth.toPx() },
                                    spacing = with(density) { amplitudeBarSpacing.toPx() }
                                )
                            )
                        }
                    }
            )

            Text(
                text = formattedDurationText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum"
                )
            )
        }
    }
}

@Preview
@Composable
private fun EchoMoodPlayerPreview() {
    EchoJournalTheme {
        val ratios = remember {
            (1..30).map {
                Random.nextFloat()
            }
        }
        EchoMoodPlayer(
            moodUi = MoodUi.STRESSED,
            playbackState = PlaybackState.PAUSED,
            playerProgress = { 0.3f },
            durationPlayed = 125.seconds,
            totalPlaybackDuration = 250.seconds,
            amplitudes = ratios,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}