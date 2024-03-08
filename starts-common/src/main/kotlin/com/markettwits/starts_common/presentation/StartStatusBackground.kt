package com.markettwits.starts_common.presentation

import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.theme.SportSouceColor

/**
 * @param 0 - Start close
 * @param 1 - The start has been postponed
 * @param 2 - Registration will start soon
 * @param 3 - Registration is open
 * @param 4 - Registration is closed, wait for the start
 * @param 6 - Start ended
 */
fun startStatusBackground(statusCode: Int): Color =
    when (statusCode) {
        1 -> SportSouceColor.SportSouceLightRed
        2 -> SportSouceColor.SportSouceRegistryCommingSoonYellow
        3 -> SportSouceColor.SportSouceRegistryOpenGreen
        5 -> SportSouceColor.SportSouceBlue
        4 -> SportSouceColor.SportSouceRegistryCommingSoonYellow
        6 -> SportSouceColor.SportSouceStartEndedPink
        else -> SportSouceColor.SportSouceBlue
    }