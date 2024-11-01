package com.markettwits.intent.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.markettwits.IntentAction
import com.markettwits.IntentActionBase

@Composable
internal actual fun rememberIntentAction() : IntentAction = remember { IntentActionBase() }