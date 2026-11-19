package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.resume

@Composable
internal actual fun rememberRegistrationPosterPicker(
    onReading: () -> Unit,
    onPicked: (RegistrationPoster?, String?) -> Unit,
): () -> Unit {
    val reading by rememberUpdatedState(onReading)
    val callback by rememberUpdatedState(onPicked)
    val scope = rememberCoroutineScope()
    return { scope.launch {
        val file = suspendCancellableCoroutine<File?> { continuation -> SwingUtilities.invokeLater {
            if (continuation.isActive) {
                val chooser = JFileChooser().apply { fileFilter = FileNameExtensionFilter("Изображения", "jpg", "jpeg", "png", "webp") }
                val selected = if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
                if (continuation.isActive) continuation.resume(selected)
            }
        } }
        if (file != null) try {
            reading()
            val proof = withContext(Dispatchers.IO) {
                require(file.length() <= MaxPosterBytes) { "Файл больше 5 МБ" }
                val bytes = file.inputStream().use { it.readNBytes(MaxPosterBytes + 1) }
                checkedRegistrationPoster(file.name, bytes, file.lastModified())
            }
            callback(proof, null)
        } catch (e: CancellationException) { throw e }
        catch (e: IllegalArgumentException) { callback(null, e.message ?: "Не удалось прочитать файл") }
        catch (_: Exception) { callback(null, "Не удалось прочитать файл") }
    }; Unit }
}
