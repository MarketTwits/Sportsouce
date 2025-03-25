package com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.components

import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File

fun openImageFileDialog(onFileSelected: (File) -> Unit) {
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"
    input.accept = "image/*"
    input.onchange = {
        val file = input.files?.item(0) as? File
        if (file != null) {
            onFileSelected(file)
        }
    }
    input.click()
}

fun ArrayBuffer.toByteArray(): ByteArray {
    return Int8Array(this).unsafeCast<ByteArray>()
}