package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components.SignInContent

@Composable
internal fun SignInScreen(component: SignInScreen) {
    val state by component.state.collectAsState()
    SignInContent(state = state, component = component)
}
