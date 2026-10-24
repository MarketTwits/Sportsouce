package com.markettwits.sportsouce.starts.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone

/** Event status; constrained horizontal layouts can opt into a single ellipsized line. */
@Composable
fun StartStatusBadge(
    statusCode: Int,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    centered: Boolean = false,
    singleLine: Boolean = false,
) {
    val tone = when (statusCode) {
        1, 2, 4 -> SauceStatusTone.Warning
        3, 5 -> SauceStatusTone.Success
        else -> SauceStatusTone.Neutral
    }
    SauceStatusBadge(
        modifier = modifier,
        label = if (compact) startStatusCompactMessage(statusCode) else startStatusMessage(statusCode),
        tone = tone,
        centered = centered,
        singleLine = singleLine,
    )
}
