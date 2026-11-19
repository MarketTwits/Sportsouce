package com.markettwits.sportsouce.profile.results.presentation.screen

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.markettwits.sportsouce.profile.results.domain.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

@Composable
internal actual fun rememberResultProofPicker(onResult: (ResultProofFile?, String?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val callback by rememberUpdatedState(onResult)
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) scope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    val resolver = context.contentResolver
                    var name = "Протокол"
                    resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                            if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) require(cursor.getLong(sizeIndex) <= MAX_PROOF_BYTES) { "Файл больше 10 МБ" }
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
                            require(output.size() + count <= MAX_PROOF_BYTES) { "Файл больше 10 МБ" }
                            output.write(buffer, 0, count)
                        }
                        output.toByteArray()
                    } ?: error("Не удалось открыть файл")
                    ResultProofFile(name, resolver.getType(uri).orEmpty(), bytes, System.currentTimeMillis())
                }
                callback(file, file.validationError())
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { callback(null, e.message?.takeIf { it == "Файл больше 10 МБ" } ?: "Не удалось прочитать файл. Выберите PDF или изображение.") }
        }
    }
    return { launcher.launch(arrayOf("application/pdf", "image/*")) }
}
