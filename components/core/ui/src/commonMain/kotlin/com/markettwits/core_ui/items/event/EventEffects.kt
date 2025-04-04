package com.markettwits.core_ui.items.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable


/**
 * A side effect that gets executed when the given [event] changes to its
 * triggered state.
 *
 * @param event Pass the state event of type [T] to be listened to from
 *     your view-state.
 * @param onConsumed In this callback you are advised to set the passed
 *     [event] to an instance of [StateEventWithContentConsumed] in your
 *     view-state (see [consumed]).
 * @param action Callback that gets called in the composition's
 *     [CoroutineContext]. Perform the actual action this [event] leads to.
 *     The actual content of the [event] will be passed as an argument.
 */
@Composable
@NonRestartableComposable
fun <T> EventEffect(
    event: StateEventWithContent<T>,
    onConsumed: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEventWithContentTriggered<T>) {
            action(event.content)
            onConsumed()
        }
    }
}
