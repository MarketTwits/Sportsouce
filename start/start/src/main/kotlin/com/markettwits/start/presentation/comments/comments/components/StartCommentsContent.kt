package com.markettwits.start.presentation.comments.comments.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.start.presentation.comments.comments.StartCommentsComponent
import com.markettwits.start.presentation.comments.comments.StartCommentsStore
import com.markettwits.start.presentation.start.components.CommentTextField
import com.markettwits.start.presentation.start.components.StartCommentsPanel

@Composable
fun StartCommentsContent(
    modifier: Modifier = Modifier,
    component: StartCommentsComponent,
    eventContent: (EventContent) -> Unit
) {
    val state by component.state.collectAsState()
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
                eventContent(it)
            }
        }
    }
}