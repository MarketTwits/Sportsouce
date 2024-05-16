package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import java.awt.FileDialog
import java.awt.Frame
import java.awt.Toolkit
import java.io.File
import java.io.FilenameFilter

fun openImageFileDialog(): File? {
    val frame = Frame()
    val fileDialog = FileDialog(frame, "Изображение профиля", FileDialog.LOAD).apply {
        isVisible = true
        file = "*.png;*.jpg;*.jpeg;*.bmp;*.gif"
    }
    fileDialog.filenameFilter = FilenameFilter { _, name ->
        name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(
            ".gif"
        )
    }
    fileDialog.isVisible = true
    val selectedFile = fileDialog.file?.let { File(fileDialog.directory, it) }
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val dialogSize = fileDialog.size
    fileDialog.setLocation(
        (screenSize.width - dialogSize.width) / 2,
        (screenSize.height - dialogSize.height) / 2
    )
    fileDialog.dispose()
    frame.dispose()
    return selectedFile
}