package com.mbarker99.echojournal.echos.presentation.echos.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import com.mbarker99.echojournal.core.presentation.designsystem.theme.buttonGradient
import com.mbarker99.echojournal.core.presentation.designsystem.theme.buttonGradientPressed
import com.mbarker99.echojournal.core.presentation.designsystem.theme.primary90
import com.mbarker99.echojournal.core.presentation.designsystem.theme.primary95

data class BubbleFloatingActionButtonColors(
    val primary: Brush,
    val primaryPressed: Brush,
    val outerCircle: Brush,
    val innerCircle: Brush,
)

@Composable
fun rememberBubbleFloatingActionButtonColors(
    primary: Brush = MaterialTheme.colorScheme.buttonGradient,
    primaryPressed: Brush = MaterialTheme.colorScheme.buttonGradientPressed,
    outerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary95),
    innerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary90),
): BubbleFloatingActionButtonColors {
    return remember(primary, primaryPressed, outerCircle, innerCircle) {
        BubbleFloatingActionButtonColors(
            primary = primary,
            primaryPressed = primaryPressed,
            outerCircle = outerCircle,
            innerCircle = innerCircle
        )
    }
}
