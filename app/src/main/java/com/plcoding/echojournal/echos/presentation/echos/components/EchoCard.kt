package com.plcoding.echojournal.echos.presentation.echos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plcoding.echojournal.core.presentation.designsystem.chips.HashtagChip
import com.plcoding.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.plcoding.echojournal.core.presentation.util.defaultShadow
import com.plcoding.echojournal.echos.presentation.components.EchoMoodPlayer
import com.plcoding.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.plcoding.echojournal.echos.presentation.model.EchoUi
import com.plcoding.echojournal.echos.presentation.preview.PreviewModels

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EchoCard(
    echo: EchoUi,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .defaultShadow(shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = echo.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = echo.formattedRecordedAt ,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            EchoMoodPlayer(
                moodUi = echo.mood,
                playbackState = echo.playbackState,
                playerProgress = { echo.playbackRatio },
                durationPlayed = echo.playbackCurrentDuration,
                totalPlaybackDuration = echo.playbackTotalDuration,
                powerRatios = echo.amplitudes,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                onTrackSizeAvailable = onTrackSizeAvailable,
                modifier = modifier.fillMaxWidth(),
            )

            if (!echo.note.isNullOrBlank()) {
                EchoExpandableText(text = echo.note)
            }


            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                echo.topics.forEach{ topic ->
                    HashtagChip(text = topic)

                }
            }
        }
    }
}

@Preview
@Composable
private fun EchoCardPreview() {
    EchoJournalTheme {
        EchoCard(
            echo = PreviewModels.echoUi,
            onTrackSizeAvailable = { },
            onPlayClick = { },
            onPauseClick = { },
        )
    }
}