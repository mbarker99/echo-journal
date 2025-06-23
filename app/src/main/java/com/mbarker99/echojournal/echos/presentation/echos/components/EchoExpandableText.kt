package com.mbarker99.echojournal.echos.presentation.echos.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme

@Composable
fun EchoExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLine: Int = 3
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var isClickable by remember {
        mutableStateOf(false)
    }

    var lastCharIndex by remember {
        mutableIntStateOf(0)
    }

    val showMoreText = stringResource(R.string.show_more)
    val showMoreTextColor = MaterialTheme.colorScheme.primary

    val textToShow = remember(text, isClickable, isExpanded) {
        buildAnnotatedString {
            when {
                isClickable && !isExpanded -> {
                    val adjustedText = text
                        .substring(
                            startIndex = 0,
                            endIndex = lastCharIndex
                        )
                        .dropLast(showMoreText.length + 3)
                        .dropLastWhile {
                            Character.isWhitespace(it) || it == '.'
                        }
                    append(adjustedText)
                    append("...")

                    withStyle(
                        style = SpanStyle(
                            color = showMoreTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(showMoreText)
                    }
                }

                else -> {
                    append(text)
                }
            }
        }
    }

    Text(
        text = textToShow,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = if (isExpanded) 100 else collapsedMaxLine,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = isClickable,
                interactionSource = null,
                indication = null
            ) {
                isExpanded = !isExpanded
            }
            .animateContentSize(),
        onTextLayout = { result ->
            if (!isExpanded && result.hasVisualOverflow) {
                isClickable = true
                lastCharIndex = result.getLineEnd(collapsedMaxLine - 1)
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun EchoExpandableTextPreview() {
    EchoJournalTheme {
        EchoExpandableText(
            text = buildString { repeat(100) { append("Hello ") } },
        )
    }

}