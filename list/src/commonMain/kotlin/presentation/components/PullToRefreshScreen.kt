package presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshScreen(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        onRefresh()
    })
    Box(modifier = modifier.fillMaxSize()) {
        content(Modifier.pullRefresh(state))
        PullRefreshIndicator(
            contentColor = Color.Black,
            refreshing = isRefreshing, state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}