package com.markettwits.sportsouce.edit_profile.image.presentation.components

import org.w3c.files.File

internal data class SelectedImageMetadata(
    val fileName: String?,
    val contentType: String?,
)

internal fun File.selectedImageMetadata(): SelectedImageMetadata =
    SelectedImageMetadata(
        fileName = name,
        contentType = type.takeIf { it.isNotBlank() }
    )
