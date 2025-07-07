package com.mbarker99.echojournal.echos.presentation.create_echo.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.designsystem.buttons.PrimaryButton
import com.mbarker99.echojournal.core.presentation.designsystem.buttons.SecondaryButton
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.mbarker99.echojournal.echos.presentation.components.MoodIconWithCaption
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMoodBottomSheet(
    selectedMood: MoodUi,
    onMoodClick: (MoodUi) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val allMoods = MoodUi.entries.toList()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )


            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                allMoods.forEach { mood ->
                    MoodIconWithCaption(
                        mood = mood,
                        isSelected = mood == selectedMood,
                        onClick = { onMoodClick(mood) },
                    )
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SecondaryButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxHeight()
                )

                PrimaryButton(
                    text = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.confirm)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectMoodBottomSheetPreview() {
    EchoJournalTheme {
        SelectMoodBottomSheet(
            selectedMood = MoodUi.STRESSED,
            onMoodClick = {  },
            onDismiss = {  },
            onConfirmClick = {  }
        )
    }

}