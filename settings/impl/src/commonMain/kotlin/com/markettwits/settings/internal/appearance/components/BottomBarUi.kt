package com.markettwits.settings.internal.appearance.components

import androidx.compose.runtime.Immutable

@Immutable
data class BottomBarUi(
    val id: Int,
    val isSelected: Boolean,
    val title: String
)