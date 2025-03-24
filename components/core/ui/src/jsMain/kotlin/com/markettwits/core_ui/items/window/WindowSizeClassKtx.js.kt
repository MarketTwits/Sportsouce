package com.markettwits.core_ui.items.window

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

@ExperimentalMaterial3WindowSizeClassApi
@Composable
actual fun calculateWindowSizeClass(): WindowSizeClass = androidx.compose.material3.windowsizeclass.calculateWindowSizeClass()