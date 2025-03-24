package com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class KindOfSportsInfo(
    val sportId: Int,
    val title: String,
    val count: Int,
    val color: Color,
    val icon: ImageVector
)
