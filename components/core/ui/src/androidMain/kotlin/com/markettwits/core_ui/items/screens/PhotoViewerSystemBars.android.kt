package com.markettwits.core_ui.items.screens

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme

@Composable
internal actual fun PhotoViewerSystemBars(controlsVisible: Boolean) {
    val view = LocalView.current
    val darkTheme = LocalDarkOrLightTheme.current
    val window = generateSequence(view) { it.parent as? View }
        .mapNotNull { (it as? DialogWindowProvider)?.window }.firstOrNull()
        ?: view.context.photoViewerActivity()?.window ?: return
    val controller = remember(window, view) { WindowInsetsControllerCompat(window, view) }
    DisposableEffect(controller) {
        val previousDim = window.attributes.dimAmount
        @Suppress("DEPRECATION") val previousNavigationColor = window.navigationBarColor
        val previousContrast = if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced else false
        @Suppress("DEPRECATION")
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = false
        window.setDimAmount(0f)
        val lightStatus = controller.isAppearanceLightStatusBars
        val lightNavigation = controller.isAppearanceLightNavigationBars
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        onDispose {
            window.setDimAmount(previousDim)
            @Suppress("DEPRECATION")
            window.navigationBarColor = previousNavigationColor
            if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = previousContrast
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.isAppearanceLightStatusBars = lightStatus
            controller.isAppearanceLightNavigationBars = lightNavigation
        }
    }
    SideEffect {
        controller.isAppearanceLightStatusBars = !darkTheme
        controller.isAppearanceLightNavigationBars = !darkTheme
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

@Composable
actual fun PhotoHeaderSystemBars(lightIcons: Boolean) {
    val view = LocalView.current
    val window = view.context.photoViewerActivity()?.window ?: return
    val controller = remember(window, view) { WindowInsetsControllerCompat(window, view) }
    DisposableEffect(controller) {
        val previous = controller.isAppearanceLightStatusBars
        onDispose { controller.isAppearanceLightStatusBars = previous }
    }
    SideEffect { controller.isAppearanceLightStatusBars = !lightIcons }
}
