package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.attafitamim.krop.core.crop.CropState
import com.attafitamim.krop.ui.ImageCropperDialog

@Composable
internal fun CropperDialog(cropState: CropState) {
    ImageCropperDialog(
        state = cropState,
        dialogProperties = ImageCropperDialogProperties,
        dialogPadding = PaddingValues(0.dp),
        dialogShape = RoundedCornerShape(0.dp),
        topBar = {
            CropperTopBar(it)
        }
    )
}
