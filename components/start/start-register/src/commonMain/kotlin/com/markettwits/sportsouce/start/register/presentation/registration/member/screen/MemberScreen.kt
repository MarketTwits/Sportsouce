package com.markettwits.sportsouce.start.register.presentation.registration.member.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AlertDialogScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.register.presentation.registration.member.component.RegistrationMemberComponent
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.MemberScreenContent
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    component: RegistrationMemberComponent
) {
    val state by component.model.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopBarWithClip(title = "Регистрация") {
                component.obtainEvent(RegistrationMemberStore.Intent.Pop)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = SportSouceColor.SportSouceLightRed,
                    snackbarData = it
                )
            }
        }
    ) {
        MemberScreenContent(
            modifier = Modifier.padding(it),
            userNumber = state.userNumber,
            statement = state.value,
            members = state.members,
            onValueChanged = {
                component.obtainEvent(RegistrationMemberStore.Intent.ChangeFiled(it))
            },
            onClickContinue = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnClickContinue)
            }
        )
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
        if (state.isClosedAllerDialog) {
            AlertDialogScreen(
                onDismissRequest = {
                    component.obtainEvent(RegistrationMemberStore.Intent.Pop)
                }, onClickOk = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnClickCloseDialog)
                })
        }
    }
}
