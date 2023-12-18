package com.markettwits.start.presentation.common

import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.theme.SportSouceColor
import io.ktor.client.utils.EmptyContent.status

typealias OnClick = () -> Unit

fun startStatusBackground(statusCode: Int): Color =
    when (statusCode) {
        2 -> SportSouceColor.SportSouceRegistryCommingSoonYellow
        3 -> SportSouceColor.SportSouceRegistryOpenGreen
        4 -> SportSouceColor.SportSouceLightRed
        6 -> SportSouceColor.SportSouceStartEndedPink
        else -> Color.Blue
    }