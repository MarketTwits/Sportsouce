package presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.markettwits.random_user.RandomUser
import presentation.components.item.UserItemCard
import presentation.store.ListStore

@Composable
fun ListScreenContent(
    state: ListStore.State,
    onClickRefresh: () -> Unit,
    onClickItem: (RandomUser) -> Unit
) {

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    if (state.isError) {
        FailedScreen(message = state.message, onClickRetry = {
            onClickRefresh()
        })
    }
    if (state.data.isNotEmpty()) {
        PullToRefreshScreen(
            isRefreshing = state.isLoading,
            onRefresh = {
                onClickRefresh()
            },
            content = {
                LazyColumn(modifier = it) {
                    items(state.data) {
                        UserItemCard(item = it) { item ->
                            onClickItem(item)
                        }
                    }
                }
            }
        )
    }
}