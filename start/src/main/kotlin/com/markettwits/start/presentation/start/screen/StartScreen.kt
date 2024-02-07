@file:JvmName("StartScreenKt")

package com.markettwits.start.presentation.start.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.start.presentation.comments.comments.StartCommentsComponent
import com.markettwits.start.presentation.comments.comments.components.StartCommentsContent
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.start.component.StartScreenComponent
import com.markettwits.start.presentation.start.store.StartScreenStore

@Composable
fun StartScreen(component: StartScreenComponent, commentsComponent: StartCommentsComponent) {
    val startData by component.start.collectAsState()

    startData.data?.let { data ->
        StartScreenContent(
            data = data,
            isLoading = startData.isLoading,
            onClickRetry = {
                component.obtainEvent(StartScreenStore.Intent.OnClickRetry)
            },
            onClickBack = {
                component.obtainEvent(StartScreenStore.Intent.OnClickBack)
            },
            onClickDistance = { distance, paymentDisabled, paymentType ->
                component.obtainEvent(
                    StartScreenStore.Intent.OnClickDistance(
                        distance,
                        paymentDisabled,
                        paymentType
                    )
                )
            },
            onClickMembers = {
                component.obtainEvent(StartScreenStore.Intent.OnClickMembers(it))
            },
            comments = { modifier ->
                StartCommentsContent(modifier = modifier, component = commentsComponent)
            }
        )
    }
    if (startData.isLoading && startData.data == null) {
        LoadingScreen()
    }
    if (startData.isError) {
        FailedScreen(
            message = startData.message,
            onClickHelp = {
            },
            onClickRetry = {
                component.obtainEvent(StartScreenStore.Intent.OnClickRetry)
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    //StartScreen(MockStartScreenComponent())
}