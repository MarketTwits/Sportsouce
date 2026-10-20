package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

/** A read-only field opens its existing action without invoking the editor or keyboard. */
@Composable
internal fun textFieldActivationModifier(enabled: Boolean, onClick: (() -> Unit)?): Modifier {
    val currentClick by rememberUpdatedState(onClick)
    return if (onClick != null && enabled) Modifier
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (up != null) { up.consume(); currentClick?.invoke() }
            }
        }
        .onPreviewKeyEvent { event ->
            if (event.key == Key.Enter || event.key == Key.Spacebar) {
                if (event.type == KeyEventType.KeyUp) currentClick?.invoke()
                true
            } else false
        }
        .semantics { role = Role.Button; onClick { currentClick?.invoke(); true } }
    else Modifier
}
