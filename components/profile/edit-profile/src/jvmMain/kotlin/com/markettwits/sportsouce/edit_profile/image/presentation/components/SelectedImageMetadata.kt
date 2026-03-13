package com.markettwits.sportsouce.edit_profile.image.presentation.components

import java.io.File
import java.net.URLConnection

internal data class SelectedImageMetadata(
    val fileName: String?,
    val contentType: String?,
)

internal fun File.selectedImageMetadata(): SelectedImageMetadata =
    SelectedImageMetadata(
        fileName = name,
        contentType = URLConnection.guessContentTypeFromName(name)
    )
