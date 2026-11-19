package com.markettwits.sportsouce.profile.registrations.presentation.management

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

@Composable
internal actual fun rememberRegistrationPosterPicker(
    onReading: () -> Unit,
    onPicked: (RegistrationPoster?, String?) -> Unit,
): () -> Unit {
    val reading by rememberUpdatedState(onReading)
    val context = LocalContext.current
    val callback by rememberUpdatedState(onPicked)
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) scope.launch {
            reading()
            try {
                val file = withContext(Dispatchers.IO) {
                    val resolver = context.contentResolver
                    var name = "Изображение"
                    resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                            if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) require(cursor.getLong(sizeIndex) <= MaxPosterBytes) { "Файл больше 5 МБ" }
                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (nameIndex >= 0) name = cursor.getString(nameIndex) ?: name
                        }
                    }
                    val bytes = resolver.openInputStream(uri)?.use { input ->
                        val output = ByteArrayOutputStream()
                        val buffer = ByteArray(8192)
                        while (true) {
                            ensureActive()
                            val count = input.read(buffer)
                            if (count < 0) break
                            require(output.size() + count <= MaxPosterBytes) { "Файл больше 5 МБ" }
                            output.write(buffer, 0, count)
                        }
                        output.toByteArray()
                    } ?: error("Не удалось открыть файл")
                    checkedRegistrationPoster(name, bytes, System.currentTimeMillis())
                }
                callback(file, null)
            } catch (e: CancellationException) { throw e }
            catch (e: IllegalArgumentException) { callback(null, e.message ?: "Не удалось прочитать файл") }
            catch (_: Exception) { callback(null, "Не удалось прочитать файл") }
        }
    }
    return { launcher.launch(posterMimeTypes) }
}
