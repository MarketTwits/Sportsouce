package com.markettwits.start_filter.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.start_filter.starts.store.StartsFilteredStore
import com.markettwits.starts_common.presentation.StartCard

@Composable
fun StartsFilteredScreen(component: StartsFilteredComponent) {
    val state by component.value.collectAsState()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        TopBarWithClip(title = "Фильтр") {
            component.obtainEvent(StartsFilteredStore.Intent.OnClickBack)
        }
        if (state.isLoading){
            LoadingFullScreen()
        }
        if (state.isError){
            FailedScreen(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                message = state.message,
                onClickHelp = { /*TODO*/ }
            ) {

            }
        }
        LazyColumn{
            items(state.starts) {
                StartCard(start = it, onItemClick = {
                    component.obtainEvent(StartsFilteredStore.Intent.OnItemClick(it))
                })
            }
        }
    }
}