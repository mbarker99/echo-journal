package com.mbarker99.echojournal.echos.presentation.create_echo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

@Composable
fun MoodIconWithCaption(
    mood: MoodUi,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .width(64.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick
            )
    ) {
        Image(
            imageVector = if (isSelected) {
                ImageVector.vectorResource(mood.iconSet.fill)
            } else {
                ImageVector.vectorResource(mood.iconSet.outline)
            },
            contentDescription = mood.title.asString(),
            modifier = Modifier.height(40.dp),
            contentScale = ContentScale.FillHeight
        )

        Text(
            text = mood.title.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MoodIconWithCaptionPreview() {
    EchoJournalTheme {
        MoodIconWithCaption(
            isSelected = true,
            mood = MoodUi.STRESSED,
            onClick = { }
        )
    }
}