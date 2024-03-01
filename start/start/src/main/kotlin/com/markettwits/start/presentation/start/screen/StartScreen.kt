@file:JvmName("StartScreenKt")

package com.markettwits.start.presentation.start.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.FullImageScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.start.presentation.comments.comments.StartCommentsComponent
import com.markettwits.start.presentation.comments.comments.components.StartCommentsContent
import com.markettwits.start.presentation.start.component.StartScreenComponent
import com.markettwits.start.presentation.start.components.StartScreenContent
import com.markettwits.start.presentation.start.store.StartScreenStore

@Composable
fun StartScreen(component: StartScreenComponent, commentsComponent: StartCommentsComponent) {
    val startData by component.start.collectAsState()
    var fullImage by rememberSaveable {
        mutableStateOf(false)
    }

    SportSouceTheme {
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
                            distanceInfo = distance,
                            paymentDisabled = paymentDisabled,
                            paymentType = paymentType
                        )
                    )
                },
                onClickMembers = {
                    component.obtainEvent(StartScreenStore.Intent.OnClickMembers(it))
                },
                onClickImage = { fullImage = !fullImage },
                comments = { modifier ->
                    StartCommentsContent(modifier = modifier, component = commentsComponent)
                },
            )
            if (fullImage) {
                FullImageScreen(image = data.image) { fullImage = !fullImage }
            }
        }
        if (startData.isLoading && startData.data == null) {
            LoadingFullScreen()
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

}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    //StartScreen(MockStartScreenComponent())
}