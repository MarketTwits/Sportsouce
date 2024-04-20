package com.markettwits.start_filter.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.items.theme.FontNunito
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
        if (state.isLoading) {
            LoadingFullScreen()
        }
        if (state.isError) {
            FailedScreen(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                message = state.message,
                onClickBack = {
                    component.obtainEvent(StartsFilteredStore.Intent.OnClickBack)
                },
                onClickRetry = {
                    component.obtainEvent(StartsFilteredStore.Intent.Launch)
                }
            )
        }
        if (state.starts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "По заданным фильтрам ничего не найдено",
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontFamily = FontNunito.regular(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        LazyColumn {
            items(state.starts) {
                StartCard(start = it, onItemClick = {
                    component.obtainEvent(StartsFilteredStore.Intent.OnItemClick(it))
                })
            }
        }
    }
}