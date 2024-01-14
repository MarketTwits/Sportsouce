package com.markettwits.review.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.review.presentation.components.actual.ActualStarts
import com.markettwits.review.presentation.components.archive.ArchiveStarts
import com.markettwits.review.presentation.components.review_menu.ReviewMenu
import com.markettwits.review.presentation.components.social_network.SocialNetwork
import com.markettwits.review.presentation.store.ReviewStore
import com.markettwits.root.RootNewsComponent
import com.markettwits.root.RootNewsScreen

@Composable
fun ReviewScreen(component: ReviewComponent, newsComponent: RootNewsComponent) {
    val state by component.value.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(10.dp)
    ) {
        if (state.actualStarts.isNotEmpty()) {
            RootNewsScreen(newsComponent)
        }
        ReviewMenu {
            component.obtainEvent(ReviewStore.Intent.OnClickMenu(it))
        }
        if (state.actualStarts.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ActualStarts(starts = state.actualStarts) {
                component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
            }
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ArchiveStarts(starts = state.archiveStarts) {
                component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
            }
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            SocialNetwork()
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                strokeCap = StrokeCap.Round,
                color = SportSouceColor.SportSouceBlue
            )
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = {
                },
                onClickRetry = {
                    component.obtainEvent(ReviewStore.Intent.Launch)
                }
            )
        }
    }
}