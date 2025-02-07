
package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.presentation.comments.component.StartCommentsComponent
import com.markettwits.start.presentation.comments.components.StartCommentsContent
import com.markettwits.start.presentation.start.component.StartScreenComponent
import com.markettwits.start.presentation.start.components.StartSupport
import com.markettwits.start.presentation.start.store.StartScreenStore
import com.markettwits.start_support.presentation.component.StartSupportComponent

@Composable
fun StartScreen(
    startComponent: StartScreenComponent,
    startCommentsComponent: StartCommentsComponent,
    startSupportComponent: StartSupportComponent,
) {
    val state by startComponent.start.collectAsState()
    var fullImage by rememberSaveable {
        mutableStateOf(false)
    }
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
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
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(it)
        ) {
            state.data?.let { data ->
                StartScreenContent(
                    data = data,
                    isLoading = state.isLoading,
                    onClickRetry = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickRetry)
                    },
                    onClickBack = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                    },
                    onClickDistance = { distance, paymentDisabled, paymentType ->
                        startComponent.obtainEvent(
                            StartScreenStore.Intent.OnClickDistance(
                                distanceInfo = distance,
                                paymentDisabled = paymentDisabled,
                                paymentType = paymentType
                            )
                        )
                    },
                    onClickDistanceNew = {
                        startComponent.obtainEvent(
                            StartScreenStore.Intent.OnClickDistanceNew(it)
                        )
                    },
                    onClickMembers = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickMembers(it))
                    },
                    onClickUrl = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickUrl(it))
                    },
                    onClickPhone = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickPhone(it))
                    },
                    onClickImage = {
                        fullImage = !fullImage
                    },
                    onClickFullAlbum = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickFullAlbum)
                    },
                    donations = { modifier ->
                        StartSupport(modifier = modifier, component = startSupportComponent) {
                            startComponent.obtainEvent(
                                StartScreenStore.Intent.TriggerEvent(
                                    it.message,
                                    it.success
                                )
                            )
                        }
                    },
                    comments = { modifier ->
                        StartCommentsContent(
                            modifier = modifier,
                            component = startCommentsComponent
                        ) {
                            startComponent.obtainEvent(
                                StartScreenStore.Intent.TriggerEvent(
                                    it.message,
                                    it.success
                                )
                            )
                        }
                    },
                )
                if (fullImage) {
                    FullImageScreen(image = data.image) { fullImage = !fullImage }
                }
            }
            if (state.isLoading && state.data == null) {
                LoadingFullScreen(
                    onClickBack = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                    }
                )
            }
            if (state.isError) {
                FailedScreen(
                    message = state.message,
                    onClickBack = {
                        startComponent.obtainEvent(StartScreenStore.Intent.OnClickBack)
                    }
                ) {
                    startComponent.obtainEvent(StartScreenStore.Intent.OnClickRetry)
                }
            }
            EventEffect(
                event = state.event,
                onConsumed = {
                    startComponent.obtainEvent(StartScreenStore.Intent.OnConsumedEvent)
                },
            ) {
                snackBarColor =
                    if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
                snackBarHostState.showLongMessageWithDismiss(message = it.message)
            }
        }
    }

}