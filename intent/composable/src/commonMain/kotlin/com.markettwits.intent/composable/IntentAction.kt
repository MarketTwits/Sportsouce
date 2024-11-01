package com.markettwits.intent.composable

import androidx.compose.runtime.Composable
import com.markettwits.IntentAction


@Composable
fun rememberIntentActionByPlatform() : IntentAction = rememberIntentAction()

@Composable
internal expect fun rememberIntentAction() : IntentAction