@file:JvmName("StartScreenKt")

package com.markettwits.start.presentation.start.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.start.presentation.comments.StartCommentsComponent
import com.markettwits.start.presentation.comments.components.StartCommentsContent
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.start.StartScreenComponent
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
            onClickDistance = {
                component.obtainEvent(StartScreenStore.Intent.OnClickDistance(it))
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