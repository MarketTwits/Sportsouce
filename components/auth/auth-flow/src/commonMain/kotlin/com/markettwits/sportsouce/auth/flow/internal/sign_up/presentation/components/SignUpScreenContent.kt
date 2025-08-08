package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.auth.flow.internal.common.AuthAlreadySomeActionBox
import com.markettwits.sportsouce.auth.flow.internal.common.AuthConsumeRowContent
import com.markettwits.sportsouce.auth.flow.internal.common.AuthWelcomeContent
import com.markettwits.sportsouce.auth.flow.internal.common.TermsAndPrivacyContent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.store.SignUpStore
import kotlinx.coroutines.launch

@Composable
internal fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpStore.State,
    onValueChanged: (SignUpStatement) -> Unit,
    onClickBack: () -> Unit,
    onNextClick: () -> Unit,
    onConsumedEvent: () -> Unit,
    onClickSignUp: () -> Unit,
    onClickSignIn: () -> Unit,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(key1 = state.currentStage) {
        coroutineScope.launch {
            // Convert 1-based stage index to 0-based pager index
            pagerState.animateScrollToPage(
                page = state.currentStage.index - 1,
                animationSpec = tween(durationMillis = 500)
            )
        }
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
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            AuthAlreadySomeActionBox(
                onClick = onClickSignIn,
                actionText = "Войти",
                descriptionText = "Уже есть аккаунт ?"
            )
        },
    ) { paddingValues ->
        AdaptivePane {
            Column(
                modifier = modifier
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthWelcomeContent()
                SignUpProgressIndicator(
                    modifier = Modifier.padding(vertical = 20.dp),
                    currentStage = state.currentStage
                )
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                ) { index ->
                    SignUpStages(
                        signUpStage = SignUpStage.fromIndex(index + 1),
                        statement = state.statement,
                        focusManager = focusManager,
                        onValueChanged = onValueChanged,
                        onNextStage = onNextClick,
                        onRegister = onClickSignUp
                    )
                }

                Spacer(Modifier.height(10.dp))

                SignUpAuthButton(
                    signUpStage = state.currentStage,
                    isLoading = state.isLoading,
                    onClickSignUp = onClickSignUp,
                    onClickNext = onNextClick,
                )

                Spacer(Modifier.height(10.dp))

                AuthConsumeRowContent(title = "Назад", onClickConsume = {
                    onClickBack()
                })

                TermsAndPrivacyContent(
                    modifier = Modifier.padding(10.dp),
                )

                EventEffect(
                    event = state.event,
                    onConsumed = { onConsumedEvent() },
                ) {
                    snackBarColor =
                        if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
                    snackBarHostState.showLongMessageWithDismiss(message = it.message)
                }
            }
            BackFloatingActionButton {
                onClickBack()
                focusManager.clearFocus()
            }
        }
    }
}