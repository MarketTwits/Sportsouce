package com.markettwits.sportsouce.unauthorized.presentation.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Suppress("DEPRECATION")
@Composable
internal actual fun GuestProfileStatusBar() {
    val view = LocalView.current
    val window = view.context.activity()?.window ?: return
    val controller = remember(view, window) { WindowInsetsControllerCompat(window, view) }
    DisposableEffect(controller) {
        val previous = controller.isAppearanceLightStatusBars
        val previousColor = window.statusBarColor
        onDispose {
            controller.isAppearanceLightStatusBars = previous
            window.statusBarColor = previousColor
        }
    }
    SideEffect {
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        controller.isAppearanceLightStatusBars = false
    }
}

private tailrec fun Context.activity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.activity()
    else -> null
}
