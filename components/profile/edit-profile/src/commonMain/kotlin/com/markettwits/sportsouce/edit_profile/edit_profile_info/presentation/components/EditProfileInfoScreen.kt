package com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarClipWithLabel
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.component.EditProfileInfoComponent
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore

@Composable
fun EditProfileInfoScreen(component: EditProfileInfoComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarColor = remember {
        SportSouceColor.SportSouceLighBlue
    }
    Scaffold(topBar = {
        TopBarClipWithLabel(
            title = "Редактировать профиль",
            goBack = {
                component.obtainEvent(EditProfileInfoStore.Intent.GoBack)
            },
            onClickLabel = {
                component.obtainEvent(EditProfileInfoStore.Intent.OnClickUpdate)
            }
        )
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackBarHostState,
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = snackBarColor,
                dismissActionContentColor = Color.White,
                snackbarData = it
            )
        }
    }
    ) { padding ->
        state.userData?.let {
            EditProfileInfoFieldsContent(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = padding.calculateTopPadding()),
                user = it,
                teams = state.teams,
                cities = state.cities,
                onUserChange = { newValue ->
                    component.obtainEvent(EditProfileInfoStore.Intent.UpdateState(newValue))
                }
            )
        }
        if (state.isLoading) {
            LoadingFullScreen(modifier = Modifier.padding(top = padding.calculateTopPadding()))
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickBack = {
                    component.obtainEvent(EditProfileInfoStore.Intent.GoBack)
                },
                onClickRetry = {
                    component.obtainEvent(EditProfileInfoStore.Intent.Retry)
                }
            )
        }
    }
    EventEffect(
        event = state.event,
        onConsumed = {
            component.obtainEvent(EditProfileInfoStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor =
            if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}