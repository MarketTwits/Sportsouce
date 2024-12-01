package com.markettwits.start.register.presentation.registration.presentation.components.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core.errors.api.SauceComposableErrorRender
import com.markettwits.core.errors.impl.rememberSauceErrorRender
import com.markettwits.core_ui.items.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.start.register.presentation.order.presentation.components.extra.StartRegistrationTopBar
import com.markettwits.start.register.presentation.registration.presentation.component.registration.StartRegistrationPageComponent
import com.markettwits.start.register.presentation.registration.presentation.component.distance.StartDistanceComponent
import com.markettwits.start.register.presentation.registration.presentation.component.pay.StartPayComponent
import com.markettwits.start.register.presentation.registration.presentation.components.distsance.StartStage
import com.markettwits.start.register.presentation.registration.presentation.components.pay.StartPay
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore

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
            Column {
                StartRegistrationTopBar(startTitle = state.registrationInfo.startTitle) {
                    component.obtainEvent(StartRegistrationPageStore.Intent.OnClickGoBack)
                }
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
            if (state.pagesState.isLoading){
                LoadingFullScreen(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                )
            }
            if (state.pagesState.error != null){
                rememberSauceErrorRender().ComposableThrowableError(
                    modifier = Modifier,
                    throwable = state.pagesState.error!!,
                    onClickGoBack = {},
                    onClickRetry = {}
                )
            }
            Children(
                stack = pages,
                animation = stackAnimation(animator = slide())
            ){ child ->
                when(val page = child.instance){
                    is StartPayComponent ->{
                        StartPay(
                            modifier = Modifier,
                            component = page
                        )
                    }
                    is StartDistanceComponent ->{
                        StartStage(
                            modifier = Modifier,
                            component = page
                        )
                    }
                }
            }
            StartStagesIndicatorContent(
                modifier = Modifier.padding(10.dp),
                currentIndex = pages.active.instance.state.value.id,
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