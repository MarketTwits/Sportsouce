package com.markettwits.intent.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.markettwits.IntentAction
import com.markettwits.IntentActionBase

@Composable
internal actual fun rememberIntentAction() : IntentAction {
    val context = LocalContext.current
    return remember {
        IntentActionBase(context)
    }
}