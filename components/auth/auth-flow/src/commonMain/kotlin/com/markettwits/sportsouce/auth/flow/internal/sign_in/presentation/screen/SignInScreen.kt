package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components.SignInContent

@Composable
internal fun SignInScreen(component: SignInScreen) {
    val state by component.state.subscribeAsState()
    val fieldState by component.fieldState.subscribeAsState()
    SignInContent(state = state, fieldState = fieldState, component = component)
}
