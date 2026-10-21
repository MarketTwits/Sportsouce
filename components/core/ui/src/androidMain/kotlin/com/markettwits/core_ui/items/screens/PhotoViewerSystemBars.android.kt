package com.markettwits.core_ui.items.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
internal actual fun PhotoViewerSystemBars(controlsVisible: Boolean) {
    val view = LocalView.current
    val window = (view.parent as? DialogWindowProvider)?.window ?: view.context.photoViewerActivity()?.window ?: return
    val controller = remember(window, view) { WindowInsetsControllerCompat(window, view) }
    DisposableEffect(controller) {
        val previousDim = window.attributes.dimAmount
        window.setDimAmount(0f)
        val lightStatus = controller.isAppearanceLightStatusBars
        val lightNavigation = controller.isAppearanceLightNavigationBars
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        onDispose {
            window.setDimAmount(previousDim)
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.isAppearanceLightStatusBars = lightStatus
            controller.isAppearanceLightNavigationBars = lightNavigation
        }
    }
    LaunchedEffect(controller, controlsVisible) {
        if (controlsVisible) controller.show(WindowInsetsCompat.Type.systemBars())
        else controller.hide(WindowInsetsCompat.Type.systemBars())
    }
}

private fun Context.photoViewerActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.photoViewerActivity()
    else -> null
}
