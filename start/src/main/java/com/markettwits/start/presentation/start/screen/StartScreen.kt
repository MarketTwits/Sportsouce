@file:JvmName("StartScreenKt")

package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.start.MockStartScreen
import com.markettwits.start.presentation.start.StartItemUi
import com.markettwits.start.presentation.start.StartScreen
import com.markettwits.start.presentation.start.component.BackFloatingActionButton
import com.markettwits.start.presentation.start.component.CommentTextField
import com.markettwits.start.presentation.start.component.CustomScreen
import com.markettwits.start.presentation.start.component.StartCommentsPanel
import com.markettwits.start.presentation.start.component.StartConditionPanel
import com.markettwits.start.presentation.start.component.StartDescription
import com.markettwits.start.presentation.start.component.StartDistances
import com.markettwits.start.presentation.start.component.StartMembersPanel
import com.markettwits.start.presentation.start.component.StartOrganizers
import com.markettwits.start.presentation.start.component.StartResult
import com.markettwits.start.presentation.start.component.StartStatus
import com.markettwits.start.presentation.start.component.StartTitle

@Composable
fun StartScreen(component: StartScreen) {
    val startData by component.start.subscribeAsState()
    val mode by component.commentMode.subscribeAsState()
    val commentUiState by component.commentsState.subscribeAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    when (startData) {
        is StartItemUi.StartItemUiSuccess -> {
            val data = (startData as StartItemUi.StartItemUiSuccess)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    CustomScreen(imageUrl = data.image)
                    val modifier = Modifier.padding(5.dp)
                    Column(modifier = modifier) {
                        StartTitle(
                            modifier = modifier,
                            title = data.title,
                            place = data.startPlace
                        )
                        StartStatus(
                            modifier = modifier,
                            status = data.startStatus, date = data.startTime
                        )
                        StartDescription(modifier = modifier, description = data.description)
                        StartDistances(
                            modifier = modifier,
                            distance = data.distanceInfo,
                            data.startStatus
                        )
                        StartOrganizers(modifier = modifier, organizer = data.organizers)
                        StartResult(
                            modifier = modifier,
                            results = data.result,
                            title = "Результаты"
                        )
                        StartResult(
                            modifier = modifier,
                            results = data.usefulLinks,
                            title = "Полезные ссылки"
                        )
                        StartConditionPanel(modifier = modifier, file = data.conditionFile)
                        StartMembersPanel(modifier = modifier, membersCount = data.membersUi.size) {
                            component.goMembers(data.membersUi)
                        }
                        StartCommentsPanel(
                            modifier = modifier,
                            data.commentsRemote,
                            onClickReply = { name: String, id: Int ->
                                component.onClickReply(replier = name, id = id)
                            })
                    }
                    CommentTextField(
                        modifier = modifier,
                        state = commentUiState,
                        mode = mode,
                        snackbarHostState = snackBarHostState,
                        onClickCloseReply = {
                            component.onClickCloseReply()
                        },
                        messageHasBeenShowed = {
                            component.messageHasBeenShowed()
                        }
                    ) { comment ->
                        component.sendComment(comment)
                    }
                }
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Snackbar(
                        contentColor = Color.White,
                        containerColor = SportSouceColor.SportSouceLightRed,
                        snackbarData = it
                    )
                }
                BackFloatingActionButton {
                    component.goBack()
                }
            }
        }

        is StartItemUi.Loading -> {
            LoadingScreen()
        }

        is StartItemUi.Error -> {
            FailedScreen(
                onClickHelp = {

                },
                onClickRetry = {
                    component.retry()
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    StartScreen(MockStartScreen())
}