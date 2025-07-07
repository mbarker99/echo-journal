package com.mbarker99.echojournal.echos.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

@Composable
fun SelectMoodRow(
    selectedMood: MoodUi?,
    onMoodClick: (MoodUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MoodUi.entries.forEach { mood ->
            MoodIconWithCaption(
                mood = mood,
                isSelected = mood == selectedMood,
                onClick = { onMoodClick(mood) },
            )
        }
    }

}