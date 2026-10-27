package com.markettwits.core_ui.items.screens

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.ComponentDialog
import androidx.activity.addCallback
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

@Composable
internal actual fun PhotoViewerOverlay(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    val parent = LocalView.current
    val composition = rememberCompositionContext()
    val dismiss by rememberUpdatedState(onDismissRequest)
    val currentContent by rememberUpdatedState(content)
    val overlay = remember(parent, composition) {
        PhotoViewerDialog(parent, composition, { dismiss() }) { currentContent() }
    }
    DisposableEffect(overlay) {
        overlay.show()
        onDispose {
            overlay.dismiss()
            overlay.contentView.disposeComposition()
        }
    }
}

/** Configure the window before show(): only Compose animates the image and its backdrop. */
private class PhotoViewerDialog(
    parent: View,
    composition: CompositionContext,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) : ComponentDialog(parent.context, android.R.style.Theme_Translucent_NoTitleBar) {
    val contentView = ComposeView(context).apply { id = View.generateViewId() }

    init {
        val overlayWindow = requireNotNull(window)
        overlayWindow.requestFeature(Window.FEATURE_NO_TITLE)
        overlayWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        overlayWindow.setWindowAnimations(0)
        overlayWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        @Suppress("DEPRECATION")
        overlayWindow.apply {
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= 28) navigationBarDividerColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= 29) {
                isNavigationBarContrastEnforced = false
                isStatusBarContrastEnforced = false
            }
        }
        overlayWindow.setDimAmount(0f)
        WindowCompat.setDecorFitsSystemWindows(overlayWindow, false)
        overlayWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        overlayWindow.attributes = overlayWindow.attributes.apply {
            if (Build.VERSION.SDK_INT >= 28) layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            if (Build.VERSION.SDK_INT >= 30) setFitInsetsTypes(0)
        }
        val root = object : FrameLayout(context), DialogWindowProvider {
            override val window: Window = overlayWindow
        }
        root.setViewTreeLifecycleOwner(parent.findViewTreeLifecycleOwner())
        root.setViewTreeViewModelStoreOwner(parent.findViewTreeViewModelStoreOwner())
        root.setViewTreeSavedStateRegistryOwner(parent.findViewTreeSavedStateRegistryOwner())
        contentView.setParentCompositionContext(composition)
        contentView.setContent(content)
        root.addView(contentView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        setContentView(root)
        overlayWindow.setLayout(MATCH_PARENT, MATCH_PARENT)
        setCanceledOnTouchOutside(false)
        onBackPressedDispatcher.addCallback(this) { onDismissRequest() }
    }
}
