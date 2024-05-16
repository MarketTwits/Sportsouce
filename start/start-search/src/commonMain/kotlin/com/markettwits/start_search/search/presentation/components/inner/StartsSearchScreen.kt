package com.markettwits.start_search.search.presentation.components.inner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start_search.search.presentation.component.StartsSearchComponent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore
import com.markettwits.starts_common.presentation.StartsScreenContent

@Composable
fun StartsSearchScreen(component: StartsSearchComponent) {
    val state by component.model.collectAsState()
    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlways,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            StartsSearchBarInner(
                modifier = Modifier.padding(10.dp),
                query = state.query,
                onQueryChanged = {
                    component.obtainEvent(StartsSearchStore.Intent.ChangeTextFiled(it))
                },
                onBrushClicked = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickBrushText)
                },
                onFilterClicked = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickFilter)
                },
                onClickBack = {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickBack)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                LoadingFullScreen()
            }
            if (state.query.isEmpty()) {
                SearchHistoryColumn(
                    items = state.searchHistory
                ) {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickHistoryItem(it))
                }
            } else {
                SearchResultColumn(
                    starts = state.starts,
                    onClickStart = { startId, startTitle ->
                        component.obtainEvent(
                            StartsSearchStore.Intent.OnClickStart(
                                startId,
                                startTitle
                            )
                        )
                    }
                )
            }
        }

    }
}