package com.markettwits.profile.internal.sign_in.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.internal.sign_in.presentation.component.SignInScreen

@Composable
internal fun SignInScreen(component: SignInScreen) {
    val state by component.state.subscribeAsState()
    val fieldState by component.fieldState.subscribeAsState()
    SignInContent(state = state, fieldState = fieldState, component = component)
}
