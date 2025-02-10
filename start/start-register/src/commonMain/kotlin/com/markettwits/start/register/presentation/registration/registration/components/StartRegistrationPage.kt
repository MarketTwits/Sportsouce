package com.markettwits.start.register.presentation.registration.registration.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.register.presentation.registration.distance.component.StartDistanceComponent
import com.markettwits.start.register.presentation.registration.distance.components.StartStage
import com.markettwits.start.register.presentation.registration.pay.component.StartPayComponent
import com.markettwits.start.register.presentation.registration.pay.components.StartPay
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationPageComponent
import com.markettwits.start.register.presentation.registration.registration.store.StartRegistrationPageStore

@Composable
fun StartRegistrationPage(
    modifier: Modifier = Modifier,
    component: StartRegistrationPageComponent
) {

    val state by component.state.collectAsState()

    val pages by component.pages.subscribeAsState()

    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLighBlue)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            StartRegistrationTopBar(title = state.registrationInfo.startTitle) {
                component.obtainEvent(StartRegistrationPageStore.Intent.OnClickGoBack)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        ) {
            if (state.pagesState.isLoading) {
                LoadingFullScreen(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                )
            }

            state.pagesState.error?.SauceErrorScreen()

            Children(
                stack = pages,
                animation = stackAnimation(animator = slide())
            ) { child ->
                when (val page = child.instance) {
                    is StartPayComponent -> {
                        StartPay(
                            modifier = Modifier,
                            component = page
                        )
                    }

                    is StartDistanceComponent -> {
                        StartStage(
                            modifier = Modifier,
                            component = page
                        )
                    }
                }
            }
            StartStagesIndicatorContent(
                modifier = Modifier.padding(10.dp),
                currentIndex = pages.active.instance.value.id,
                startRegistrationStage = state.stages
            )
        }
    }
    EventEffect(
        event = state.eventWithContent,
        onConsumed = {
            component.obtainEvent(StartRegistrationPageStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor =
            if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}