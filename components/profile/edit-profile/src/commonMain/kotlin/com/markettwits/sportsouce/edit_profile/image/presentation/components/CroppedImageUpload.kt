package com.markettwits.sportsouce.edit_profile.image.presentation.components

internal data class CroppedImageUpload(
    val fileName: String,
    val contentType: String,
    val compressionFormat: ImageBitmapEncodingFormat,
)

internal fun croppedImageUploadFor(
    sourceFileName: String?,
    sourceContentType: String?,
): CroppedImageUpload {
    val sourceName = sourceFileName?.substringBeforeLast('.')?.takeIf { it.isNotBlank() }
        ?: "profile_image"

    val normalizedType = sourceContentType
        ?.substringBefore(';')
        ?.trim()
        ?.lowercase()

    val sourceExtension = sourceFileName
        ?.substringAfterLast('.', "")
        ?.trim()
        ?.lowercase()
        ?.takeIf { it.isNotBlank() }

    val output = when {
        normalizedType == "image/png" || sourceExtension == "png" -> {
            CroppedImageUpload(
                fileName = "$sourceName.png",
                contentType = "image/png",
                compressionFormat = ImageBitmapEncodingFormat.PNG
            )
        }

        normalizedType == "image/jpeg" ||
                normalizedType == "image/jpg" ||
                sourceExtension == "jpg" ||
                sourceExtension == "jpeg" ||
                normalizedType == "image/heic" ||
                normalizedType == "image/heif" ||
                sourceExtension == "heic" ||
                sourceExtension == "heif" -> {
            CroppedImageUpload(
                fileName = "$sourceName.jpg",
                contentType = "image/jpeg",
                compressionFormat = ImageBitmapEncodingFormat.JPEG
            )
        }

        normalizedType?.startsWith("image/") == true -> {
            CroppedImageUpload(
                fileName = "$sourceName.png",
                contentType = "image/png",
                compressionFormat = ImageBitmapEncodingFormat.PNG
            )
        }

        else -> {
            CroppedImageUpload(
                fileName = "$sourceName.jpg",
                contentType = "image/jpeg",
                compressionFormat = ImageBitmapEncodingFormat.JPEG
            )
        }
    }

    return output
}
