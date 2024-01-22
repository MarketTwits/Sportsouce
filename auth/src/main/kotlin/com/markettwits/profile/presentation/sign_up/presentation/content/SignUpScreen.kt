package com.markettwits.profile.presentation.sign_up.presentation.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.profile.presentation.sign_up.presentation.SignUpComponent
import com.markettwits.profile.presentation.sign_up.presentation.content.components.SignUpScreenContent
import com.markettwits.profile.presentation.sign_up.presentation.store.SignUpStore

@Composable
fun SignUpScreen(component: SignUpComponent) {
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
        }
    )
}