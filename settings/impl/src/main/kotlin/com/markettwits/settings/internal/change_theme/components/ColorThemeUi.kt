package com.markettwits.settings.internal.change_theme.components

import androidx.compose.runtime.Immutable

@Immutable
data class ColorThemeUi(
    val id: Int,
    val title: String,
    val checked: Boolean
)