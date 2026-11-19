package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.runtime.*
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array

@Composable
internal actual fun rememberRegistrationPosterPicker(
    onReading: () -> Unit,
    onPicked: (RegistrationPoster?, String?) -> Unit,
): () -> Unit {
    val reading by rememberUpdatedState(onReading)
    val callback by rememberUpdatedState(onPicked)
    var disposed by remember { mutableStateOf(false) }
    DisposableEffect(Unit) { onDispose { disposed = true } }
    return {
        val input = (document.createElement("input") as HTMLInputElement).apply { type = "file"; accept = posterMimeTypes.joinToString(",") }
        input.onchange = {
            input.files?.get(0)?.let { file ->
                if (disposed) Unit
                else if (file.size.toDouble() > MaxPosterBytes) callback(null, "Файл больше 5 МБ")
                else {
                    reading()
                    val reader = FileReader()
                    reader.onload = {
                        if (!disposed) {
                            val array = Int8Array(reader.result as ArrayBuffer)
                            val bytes = ByteArray(array.length) { index -> array.asDynamic()[index] as Byte }
                            val poster = try {
                                checkedRegistrationPoster(file.name, bytes, file.lastModified.toLong())
                            } catch (e: IllegalArgumentException) {
                                callback(null, e.message ?: "Не удалось прочитать файл")
                                null
                            }
                            if (poster != null) callback(poster, null)
                        }
                        null
                    }
                    reader.onerror = { if (!disposed) callback(null, "Не удалось прочитать файл"); null }
                    reader.readAsArrayBuffer(file)
                }
            }
            null
        }
        input.click()
    }
}
