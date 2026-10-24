package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.runtime.*
import com.markettwits.sportsouce.profile.results.domain.*
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array

@Composable
internal actual fun rememberResultProofPicker(onResult: (ResultProofFile?, String?) -> Unit): () -> Unit {
    val callback by rememberUpdatedState(onResult)
    var disposed by remember { mutableStateOf(false) }
    DisposableEffect(Unit) { onDispose { disposed = true } }
    return {
        val input = (document.createElement("input") as HTMLInputElement).apply { type = "file"; accept = ".pdf,image/*" }
        input.onchange = {
            input.files?.get(0)?.let { file ->
                if (file.size.toDouble() > MAX_PROOF_BYTES) callback(null, "Файл больше 10 МБ")
                else {
                    val reader = FileReader()
                    reader.onload = {
                        if (!disposed) {
                            val array = Int8Array(reader.result as ArrayBuffer)
                            val bytes = ByteArray(array.length) { index -> array.asDynamic()[index] as Byte }
                            val proof = ResultProofFile(file.name, file.type, bytes, file.lastModified.toLong())
                            callback(proof, proof.validationError())
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
