package com.skladpro.android.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.skladpro.android.presentation.navigation.SkladProNavGraph
import com.skladpro.android.ui.theme.SkladProTheme

@Composable
fun SkladProApp() {
    var darkTheme by rememberSaveable { mutableStateOf(false) }

    SkladProTheme(darkTheme = darkTheme) {
        SkladProNavGraph(
            darkTheme = darkTheme,
            onDarkThemeChange = { darkTheme = it }
        )
    }
}
