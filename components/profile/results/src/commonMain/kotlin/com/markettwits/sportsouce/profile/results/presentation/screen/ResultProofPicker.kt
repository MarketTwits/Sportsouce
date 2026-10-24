package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.runtime.Composable
import com.markettwits.sportsouce.profile.results.domain.ResultProofFile

@Composable
internal expect fun rememberResultProofPicker(onResult: (ResultProofFile?, String?) -> Unit): () -> Unit
