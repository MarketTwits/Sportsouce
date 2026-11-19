package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
internal actual fun rememberStartVoiceInput(
    onRecognized: (String) -> Unit,
    onError: (String) -> Unit,
): (() -> Unit)? {
    var isListening by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isListening = false
        if (result.resultCode == Activity.RESULT_CANCELED) return@rememberLauncherForActivityResult

        val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstNotNullOfOrNull { it.trim().takeIf(String::isNotEmpty) }
        if (result.resultCode == Activity.RESULT_OK && text != null) {
            onRecognized(text)
        } else {
            onError("Не удалось распознать речь. Попробуйте ещё раз.")
        }
    }
    return launch@{
        if (isListening) return@launch
        isListening = true
        try {
            launcher.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Назовите старт")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            })
        } catch (_: ActivityNotFoundException) {
            isListening = false
            onError("На устройстве недоступен голосовой ввод.")
        } catch (_: SecurityException) {
            isListening = false
            onError("Не удалось запустить голосовой ввод.")
        }
    }
}
