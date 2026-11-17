package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.runtime.Composable

@Composable
internal actual fun rememberStartVoiceInput(
    onRecognized: (String) -> Unit,
    onError: (String) -> Unit,
): (() -> Unit)? = null
