package com.markettwits.start.presentation.comments.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.comments.StartCommentsComponent
import com.markettwits.start.presentation.comments.StartCommentsStore
import com.markettwits.start.presentation.start.component.CommentTextField
import com.markettwits.start.presentation.start.component.StartCommentsPanel

@Composable
fun StartCommentsContent(modifier: Modifier = Modifier, component: StartCommentsComponent) {
    val state by component.state.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }
    Box(modifier = Modifier) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            StartCommentsPanel(
                modifier = modifier.padding(horizontal = 5.dp),
                commentsRemote = state.comments,
                onClickReply = { name: String, id: Int ->
                    component.obtainEvent(
                        StartCommentsStore.Intent.OnClickReply(
                            replier = name,
                            commentId = id
                        )
                    )
                })
            CommentTextField(
                modifier = Modifier,
                mode = state.mode,
                onClickCloseReply = {
                    component.obtainEvent(StartCommentsStore.Intent.OnClickCloseReply)
                },
                isLoading = state.isLoading,
            ) { comment ->
                component.obtainEvent(StartCommentsStore.Intent.OnClickSendComment(comment))
            }
            EventEffect(
                event = state.event,
                onConsumed = {
                    component.obtainEvent(StartCommentsStore.Intent.OnConsumedEvent)
                },
            ) {
                snackBarColor =
                    if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
                snackBarHostState.showLongMessageWithDismiss(message = it.message)
            }
        }
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackBarHostState,
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = snackBarColor,
                snackbarData = it
            )
        }
    }
}