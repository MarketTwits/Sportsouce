package com.markettwits.sportsouce.edit_profile.social_network.presentation.screen

import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.edit_profile.change_password.presentation.component.SaveChangesButton
import com.markettwits.sportsouce.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.sportsouce.edit_profile.social_network.presentation.components.ProfileSocialNetworkContent
import com.markettwits.sportsouce.edit_profile.social_network.presentation.components.ProfileSocialNetworkInfo
import com.markettwits.sportsouce.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore

@Composable
fun ProfileSocialNetworkScreen(component: EditProfileSocialNetworkComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val focusManager = LocalFocusManager.current
    var feedbackSuccess by remember { mutableStateOf(true) }
    val feedbackColors = if (feedbackSuccess) SauceTheme.colors.success else SauceTheme.colors.danger
    Scaffold(
        topBar = {
            TopBarWithClip(
                title = "Социальные сети",
                goBack = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.GoBack)
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = feedbackColors.content,
                    containerColor = feedbackColors.container,
                    dismissActionContentColor = feedbackColors.content,
                    snackbarData = it
                )
            }
        },
        bottomBar = {
            if (state.data == null) return@Scaffold
            AdaptivePane(fillHeight = false) { SaveChangesButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding().padding(horizontal = 16.dp, vertical = 12.dp),
                loading = state.isLoading,
                onClick = {
                    component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnCLickUpdate)
                    focusManager.clearFocus()
                }
            ) }
        }
    ) { padding ->
        AdaptivePane {
            state.data?.let {
                Column(
                    modifier = Modifier
                        .padding(padding).consumeWindowInsets(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    ProfileSocialNetworkInfo(modifier = Modifier.align(Alignment.CenterHorizontally))
                    ProfileSocialNetworkContent(
                        modifier = Modifier.padding(vertical = 10.dp),
                        user = it,
                        onUserChange = {
                            component.obtainEvent(EditProfileSocialNetworkStore.Intent.UpdateState(it))
                        }
                    )


                }
            }
            if (state.isLoading && state.data == null) {
                LoadingFullScreen(
                    modifier = Modifier.padding(top = padding.calculateTopPadding())
                )
            }
        }
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(EditProfileSocialNetworkStore.Intent.OnConsumedEvent)
            },
        ) {
            feedbackSuccess = it.success
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}
