package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable

/** Match status icons to a photo cover, then restore the app's appearance when leaving. */
@Composable
expect fun PhotoHeaderSystemBars(lightIcons: Boolean)

/** Keep the host layout stable while the photo viewer temporarily hides Android's system bars. */
@Composable
expect fun photoViewerHostInsets(): WindowInsets
