package com.markettwits.sportsouce.starts.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.starts.common.domain.StartsListItem


fun startKindOfSportsColor(kindOfSports: StartsListItem.KindOfSport): Color {
    return when (kindOfSports.name.lowercase()) {
        "велоспорт" -> Color(0xFFBA1EE5)
        "триатлон" -> Color(0xFFD81B60)
        "плавание" -> Color(0xFF00ACC1)
        "бег" -> Color(0xFF43A047)
        "гонка с препятствиями" -> Color(0xFFF4511E)
        "лыжные гонки" -> SportSouceColor.SportSouceLighBlue
        else -> Color.Gray
    }
}

fun startKindOfSportsIcon(kindOfSports: StartsListItem.KindOfSport): ImageVector {
    return when (kindOfSports.name.lowercase()) {
        "велоспорт" -> Icons.AutoMirrored.Filled.DirectionsBike
        "триатлон" -> Icons.Default.Flag
        "плавание" -> Icons.Default.Pool
        "бег" -> Icons.AutoMirrored.Filled.DirectionsRun
        "гонка с препятствиями" -> Icons.Default.Terrain
        "лыжные гонки" -> Icons.Default.DownhillSkiing
        else -> Icons.Default.Flag
    }
}