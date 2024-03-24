package com.markettwits.schedule.schedule.presentation.components.list.pager

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class PagerTopBar(
    val title: String,
    val selected: Boolean = false,
    val icon: ImageVector? = null
)