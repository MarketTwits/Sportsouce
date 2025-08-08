package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.auth.flow.internal.common.AuthButton
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage

@Composable
fun SignUpAuthButton(
    modifier: Modifier = Modifier,
    signUpStage: SignUpStage,
    isLoading: Boolean,
    onClickNext: () -> Unit,
    onClickSignUp: () -> Unit,
) {
    val title = when (signUpStage) {
        SignUpStage.THIRD -> "Зарегистрироваться"
        else -> "Далее"
    }
    AuthButton(
        modifier = modifier,
        title = title,
        enabled = !isLoading,
        loading = isLoading,
        onClick = {
            when (signUpStage) {
                SignUpStage.FIRST -> onClickNext()
                SignUpStage.SECOND -> onClickNext()
                SignUpStage.THIRD -> onClickSignUp()
            }
        }
    )
}