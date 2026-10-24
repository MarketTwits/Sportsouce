package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.runtime.*
import com.markettwits.sportsouce.profile.results.domain.*
import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Files
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.resume

@Composable
internal actual fun rememberResultProofPicker(onResult: (ResultProofFile?, String?) -> Unit): () -> Unit {
    val callback by rememberUpdatedState(onResult)
    val scope = rememberCoroutineScope()
    return { scope.launch {
        val file = suspendCancellableCoroutine<File?> { continuation -> SwingUtilities.invokeLater {
            if (continuation.isActive) {
                val chooser = JFileChooser().apply { fileFilter = FileNameExtensionFilter("PDF и изображения", "pdf", "jpg", "jpeg", "png", "webp", "heic", "gif") }
                val selected = if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
                if (continuation.isActive) continuation.resume(selected)
            }
        } }
        if (file != null) try {
            val proof = withContext(Dispatchers.IO) {
                require(file.length() <= MAX_PROOF_BYTES) { "Файл больше 10 МБ" }
                val bytes = file.inputStream().use { it.readNBytes(MAX_PROOF_BYTES + 1) }
                ResultProofFile(file.name, Files.probeContentType(file.toPath()) ?: if (file.extension.lowercase() == "pdf") "application/pdf" else "application/octet-stream", bytes, file.lastModified())
            }
            callback(proof, proof.validationError())
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { callback(null, if (e.message == "Файл больше 10 МБ") e.message else "Не удалось прочитать файл") }
    }; Unit }
}
