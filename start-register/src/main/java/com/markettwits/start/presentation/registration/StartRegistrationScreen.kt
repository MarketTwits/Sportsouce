package com.markettwits.start.presentation.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.components.openWebPage
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.event.isTriggered
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.registration.components.LoadingScreen
import com.markettwits.start.presentation.registration.components.StartRegistrationTopBar
import com.markettwits.start.presentation.registration.components.content.StartRegistrationSuccessContent
import com.markettwits.start.presentation.registration.store.StartRegistrationStore

@Composable
fun StartRegistrationScreen(component: StartRegistrationComponent) {
    val state by component.value.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }

    Scaffold(
        topBar = {
            StartRegistrationTopBar(goBack = {
                component.obtainEvent(StartRegistrationStore.Intent.GoBack)
            })
        },
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
        containerColor = Color.White
    ) {
        val statement = state.startStatement
        if (statement != null) {
            StartRegistrationSuccessContent(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                statement = statement,
                onClickSave = {
                    component.obtainEvent(StartRegistrationStore.Intent.OnClickSave)
                },
                onClickPay = {
                    component.obtainEvent(StartRegistrationStore.Intent.OnClickPay)
                },
                onValueChanged = {
                    component.obtainEvent(StartRegistrationStore.Intent.ChangeFiled(it))
                },
                onPromoChanged = {
                    component.obtainEvent(StartRegistrationStore.Intent.ChangePromo(it))
                }
            )
        }
        val context = LocalContext.current
        EventEffect(
            event = state.testEvent,
            onConsumed = {
                component.obtainEvent(StartRegistrationStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarColor = if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
//            if (state.message.isNotEmpty()) {
//                openWebPage(state.message, context)
//            }
        }
        if (state.isLoading) {
            LoadingScreen()
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = { /*TODO*/ }
            ) {
            }
        }
    }
}
//}