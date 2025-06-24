package com.mbarker99.echojournal.echos.presentation.echos.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.mbarker99.echojournal.core.presentation.designsystem.theme.Microphone
import com.mbarker99.echojournal.core.presentation.designsystem.theme.Pause
import com.mbarker99.echojournal.core.presentation.designsystem.theme.buttonGradient
import com.mbarker99.echojournal.core.presentation.designsystem.theme.primary90
import com.mbarker99.echojournal.core.presentation.designsystem.theme.primary95

private const val PRIMARY_BUTTON_BUBBLE_SIZE_DP = 120
private const val SECONDARY_BUTTON_SIZE_DP = 48

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoRecordingBottomSheet(
    formattedRecordDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        SheetContent(
            formattedRecordDuration = formattedRecordDuration,
            isRecording = isRecording,
            onDismiss = onDismiss,
            onResumeClick = onResumeClick,
            onPauseClick = onPauseClick,
            onCompleteRecording = onCompleteRecording
        )
    }
}

@Composable
fun SheetContent(
    formattedRecordDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onResumeClick: () -> Unit,
    onPauseClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryBubbleSize = PRIMARY_BUTTON_BUBBLE_SIZE_DP.dp
    val secondaryButtonSize = SECONDARY_BUTTON_SIZE_DP.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isRecording) {
                    stringResource(R.string.recording_your_memories)
                } else {
                    stringResource(R.string.recording_paused)
                },
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = formattedRecordDuration,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum",
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant

            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledIconButton(
                onClick = onDismiss,
                modifier = Modifier.size(secondaryButtonSize),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel_recording)
                )
            }

            val interactionSource = remember {
                MutableInteractionSource()
            }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(
                modifier = Modifier
                    .size(primaryBubbleSize)
                    .background(
                        color = if (isRecording) MaterialTheme.colorScheme.primary95 else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(10.dp)
                    .background(
                        color = if (isRecording) MaterialTheme.colorScheme.primary90 else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(16.dp)
                    .background(
                        brush = MaterialTheme.colorScheme.buttonGradient,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = if (isRecording) onCompleteRecording else onResumeClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Check else Icons.Filled.Microphone,
                    contentDescription = if (isRecording) stringResource(R.string.complete_recording) else stringResource(
                        R.string.resume_recording
                    ),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            FilledIconButton(
                onClick = if (isRecording) onPauseClick else onCompleteRecording,
                modifier = Modifier.size(secondaryButtonSize),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Filled.Pause else Icons.Default.Check,
                    contentDescription = if (isRecording) stringResource(R.string.pause_recording) else stringResource(
                        R.string.complete_recording
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SheetContentPreview() {
    EchoJournalTheme {
        SheetContent(
            formattedRecordDuration = "00:03:45",
            isRecording = false,
            onDismiss = { },
            onResumeClick = { },
            onPauseClick = { },
            onCompleteRecording = { }
        )
    }
}
