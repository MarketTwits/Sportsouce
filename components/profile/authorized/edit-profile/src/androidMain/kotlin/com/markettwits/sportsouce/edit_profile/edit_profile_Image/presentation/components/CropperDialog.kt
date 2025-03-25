package com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.components


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.CropperStyle
import com.mr0xf00.easycrop.ui.ImageCropperDialog

@Composable
internal fun CropperDialog(cropState: CropState) {
    ImageCropperDialog(
        state = cropState,
        dialogProperties = ImageCropperDialogProperties,
        dialogPadding = PaddingValues(0.dp),
        dialogShape = RoundedCornerShape(0.dp),
        style = CropperStyle(
            backgroundColor = MaterialTheme.colorScheme.primary,
        ),
        topBar = {
            CropperTopBar(it)
        }
    )
}