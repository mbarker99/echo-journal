package com.mbarker99.echojournal.core.presentation.designsystem.textfields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mbarker99.echojournal.core.presentation.designsystem.theme.EchoJournalTheme

@Composable
fun TransparentTextField(
    text: String,
    onValueChange:  (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String? = null,
    hintColor: Color = MaterialTheme.colorScheme.outlineVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        singleLine = singleLine,
        decorationBox = { innerTextField ->  
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isBlank() && hintText != null) {
                    Text(
                        text = hintText,
                        color = hintColor,
                        style = textStyle
                    )
                } else {
                    innerTextField()
                }
            }
        }
    )
    
}

@Preview
@Composable
private fun TransparentTextFieldPreview() {
    EchoJournalTheme {
        TransparentTextField(
            text = "Hello world!",
            onValueChange = {},
            hintText = "Hint"
        )
    }
    
}