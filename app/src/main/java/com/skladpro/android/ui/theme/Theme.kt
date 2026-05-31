package com.skladpro.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = WarehouseGreen,
    secondary = SteelBlue,
    tertiary = Amber,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = WarehouseGreenDark,
    secondary = SteelBlue,
    tertiary = Amber,
    error = ErrorRed
)

@Composable
fun SkladProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
