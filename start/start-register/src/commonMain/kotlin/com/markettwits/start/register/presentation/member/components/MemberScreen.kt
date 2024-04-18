package com.markettwits.start.register.presentation.member.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.base_screen.AlertDialogScreen
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore

@Composable
fun MemberScreen(component: RegistrationMemberComponent) {
    val state by component.model.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var alertDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopBarWithClip(title = "Регистрация") {
                alertDialog = true
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
        if (alertDialog) {
            AlertDialogScreen(
                onDismissRequest = {
                    alertDialog = false
                }, onClickOk = {
                    alertDialog = false
                    component.obtainEvent(RegistrationMemberStore.Intent.Pop)
                })
        }
    }
}
