package com.markettwits.intent.composable

import androidx.compose.runtime.Composable
import com.markettwits.EmptyIntentAction
import com.markettwits.IntentAction

@Composable
internal actual fun rememberIntentAction(): IntentAction {
    return EmptyIntentAction()
}