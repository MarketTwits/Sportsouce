package com.markettwits.edit_profile.social_network.presentation.components

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
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarClipWithLabel
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore

@Composable
fun ProfileSocialNetworkScreen(component: EditProfileSocialNetworkComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    SportSouceTheme {
        var snackBarColor = remember {
            SportSouceColor.SportSouceLighBlue
        }
        Scaffold(topBar = {
            TopBarClipWithLabel(
                title = "Редактировать профиль",
                goBack = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.GoBack)
                },
                onClickLabel = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnCLickUpdate)
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
            state.data?.let {
                ProfileSocialNetworkContent(
                    modifier = Modifier
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(top = padding.calculateTopPadding()),
                    user = it,
                    onUserChange = {
                        component.obtainEvent(EditProfileSocialNetworkStore.Intent.UpdateState(it))
                    }
                )
            }
            if (state.isLoading) {
                LoadingFullScreen(modifier = Modifier.padding(top = padding.calculateTopPadding()))
            }
        }
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarColor =
                if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}