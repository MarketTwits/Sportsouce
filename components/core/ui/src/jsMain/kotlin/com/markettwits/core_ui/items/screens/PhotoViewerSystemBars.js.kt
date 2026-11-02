package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.runtime.Composable

@Composable
internal actual fun PhotoViewerOverlay(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Popup(popupPositionProvider = remember { PhotoViewerPositionProvider }, onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true, clippingEnabled = false), content = content)
}

@Composable
internal actual fun PhotoViewerSystemBars(controlsVisible: Boolean) = Unit

@Composable
actual fun PhotoHeaderSystemBars(lightIcons: Boolean) = Unit

@Composable
actual fun photoViewerHostInsets(): WindowInsets = WindowInsets.systemBars
