package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import java.awt.FileDialog
import java.awt.Frame
import java.awt.Toolkit
import java.io.File
import java.io.FilenameFilter

fun openImageFileDialog(): File? {
    val frame = Frame().apply {
        isVisible = false
    }
    val fileDialog = FileDialog(frame, "Изображение профиля", FileDialog.LOAD).apply {
        filenameFilter = FilenameFilter { _, name ->
            name.endsWith(".png", ignoreCase = true) ||
                    name.endsWith(".jpg", ignoreCase = true) ||
                    name.endsWith(".jpeg", ignoreCase = true) ||
                    name.endsWith(".bmp", ignoreCase = true) ||
                    name.endsWith(".gif", ignoreCase = true)
        }
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val dialogSize = size
        setLocation(
            (screenSize.width - dialogSize.width) / 2,
            (screenSize.height - dialogSize.height) / 2
        )
        isVisible = true
    }
    val selectedFile = fileDialog.file?.let { File(fileDialog.directory, it) }
    fileDialog.dispose()
    frame.dispose()
    return selectedFile
}