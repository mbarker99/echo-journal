package com.plcoding.echojournal.core.presentation.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/*private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)*/

private val LightColorScheme = lightColorScheme(
    primary = Primary30,
    secondary = Secondary30,
    onPrimary = Primary100,
    primaryContainer = Primary50,
    onPrimaryContainer = Color(0xFFEEF0FF),
    inversePrimary = Secondary80,
    secondaryContainer = Secondary50,
    background = NeutralVariant99,
    surface = Primary100,
    onSurface = NeutralVariant10,
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = NeutralVariant30,
    onError = Error100,
    errorContainer = Error95,
    onErrorContainer = Error20,
    outline = NeutralVariant50,
    outlineVariant = NeutralVariant80,
)

@Composable
fun EchoJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}