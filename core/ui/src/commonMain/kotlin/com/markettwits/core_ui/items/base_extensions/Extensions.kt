package com.markettwits.core_ui.items.base_extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.net.URI


inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit,
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}


inline fun Modifier.customRippleClickable(
    crossinline onClick: () -> Unit,
    bounded: Boolean = true,
    radius: Dp = 250.dp,
    color: androidx.compose.ui.graphics.Color? = null,
): Modifier = composed {
    val currentColor = color ?: MaterialTheme.colorScheme.secondary.copy(0.2f)
    val ripple = rememberRipple(
        bounded = bounded,
        radius = radius,
        color = currentColor
    )
    clickable(
        indication = ripple,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun String.extractSiteName(): String {
    return try {
        val uri =
            URI(if (this.startsWith("http://") || this.startsWith("https://")) this else "http://$this")
        var host = uri.host
        if (host != null) {
            host = if (host.startsWith("www.")) host.substring(4) else host
            host = host.substringBefore('.')
        }
        host
    } catch (e: Exception) {
        ""
    }
}

