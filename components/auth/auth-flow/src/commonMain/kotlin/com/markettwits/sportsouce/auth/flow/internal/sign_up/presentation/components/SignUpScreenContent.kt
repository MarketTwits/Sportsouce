package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.auth.flow.internal.common.AuthButton
import com.markettwits.sportsouce.auth.flow.internal.common.ConsumeRowContent
import com.markettwits.sportsouce.auth.flow.internal.common.WelcomeContent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore

@Composable
internal fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpStore.State,
    onValueChanged: (SignUpStatement) -> Unit,
    onClickBack: () -> Unit,
    onConsumedEvent: () -> Unit,
    onClickSignUp: () -> Unit
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val localModifier = modifier.padding(10.dp)
            WelcomeContent()
            SignUpScreenFields(
                modifier = localModifier,
                statement = state.statement,
                onValueChanged = onValueChanged
            )
            AuthButton(
                modifier = localModifier,
                "Зарегистрироваться",
                enabled = !state.isLoading,
                loading = state.isLoading
            ) {
                onClickSignUp()
            }
            ConsumeRowContent(title = "Назад", onClickConsume = onClickBack)
            EventEffect(
                event = state.event,
                onConsumed = { onConsumedEvent() },
            ) {
                snackBarColor =
                    if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
                snackBarHostState.showLongMessageWithDismiss(message = it.message)
            }
        }
    }
}