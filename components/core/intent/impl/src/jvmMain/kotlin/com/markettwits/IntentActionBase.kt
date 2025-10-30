package com.markettwits

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class IntentActionBase : IntentAction {
    override fun openWebPage(url: String) {
        val desktop = Desktop.getDesktop()
        desktop.browse(URI.create(url))
    }

    override fun openPhone(phone: String) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    val uri = URI("tel:$phone")
                    desktop.browse(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                println("Browsing action is not supported on your desktop environment")
            }
        } else {
            println("Desktop is not supported on your system")
        }
    }

    override fun shareImage(byteArray: ByteArray) {
        val chooser = JFileChooser().apply {
            dialogTitle = "Save image"
            fileFilter = FileNameExtensionFilter("PNG Image", "png")
        }

        val result = chooser.showSaveDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            var file = chooser.selectedFile
            if (!file.name.endsWith(".png", ignoreCase = true)) {
                file = File(file.absolutePath + ".png")
            }
            FileOutputStream(file).use { it.write(byteArray) }
        }
    }

    override fun copyToSystemBuffer(text: String) {
        Toolkit.getDefaultToolkit()
            .systemClipboard
            .setContents(StringSelection(text), null)
    }

    override fun sharePlainText(text: String) = Unit
}