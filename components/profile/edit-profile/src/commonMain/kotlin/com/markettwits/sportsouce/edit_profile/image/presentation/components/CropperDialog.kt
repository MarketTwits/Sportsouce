package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.attafitamim.krop.core.crop.CropState
import com.attafitamim.krop.core.crop.DefaultCropperStyle
import com.attafitamim.krop.core.crop.LocalCropperStyle
import com.attafitamim.krop.ui.CropperPreview
import com.attafitamim.krop.ui.DefaultControls
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.SauceTopBarBackdrop

@Composable
internal fun CropperDialog(cropState: CropState) {
    // Keep the library's crop state, preview and controls; only the toolbar shell belongs to us.
    LaunchedEffect(cropState) { cropState.setInitialState(DefaultCropperStyle) }
    CompositionLocalProvider(LocalCropperStyle provides DefaultCropperStyle, LocalNavigationViewport provides null) {
        Dialog(onDismissRequest = { cropState.done(accept = false) }, properties = ImageCropperDialogProperties) {
            SauceScaffold(topBar = { CropperTopBar(cropState) }) { padding ->
                SauceTopBarBackdrop(Modifier.fillMaxSize()) {
                    CropperPreview(state = cropState, modifier = Modifier.fillMaxSize())
                    Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                        DefaultControls(cropState)
                    }
                }
            }
        }
    }
}
