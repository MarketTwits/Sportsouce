package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.component.SignUpComponent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components.SignUpScreenContent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore

@Composable
internal fun SignUpScreen(component: SignUpComponent) {
    val state by component.state.collectAsState()

    SignUpScreenContent(
        state = state,
        onValueChanged = {
            component.obtainEvent(SignUpStore.Intent.ChangeValue(it))
        },
        onClickSignUp = {
            component.obtainEvent(SignUpStore.Intent.OnClickRegister)
        },
        onConsumedEvent = {
            component.obtainEvent(SignUpStore.Intent.OnConsumedEvent)
        },
        onClickBack = {
            component.obtainEvent(SignUpStore.Intent.OnClickBack)
        }
    )
}