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
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start_search.search.presentation.component.StartsSearchComponent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore
import com.markettwits.starts_common.presentation.StartsScreenContent
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

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
            if (state.query.isEmpty()) {
                SearchHistoryColumn(
                    items = state.searchHistory
                ) {
                    component.obtainEvent(StartsSearchStore.Intent.OnClickHistoryItem(it))
                }
            } else {
                state.starts.let { starts ->
                    if (starts.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "РЕЗУЛЬТАТЫ ПОИСКА",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.bold,
                            fontSize = 12.sp,
                            overflow = TextOverflow.Visible
                        )
                        StartsScreenContent(
                            items = starts
                        ) { startId ->
                            val startTitle = starts.find { it.id == startId }?.name
                            if (startTitle != null)
                                component.obtainEvent(
                                    StartsSearchStore.Intent.OnClickStart(
                                        startId,
                                        startTitle
                                    )
                                )
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.CenterHorizontally),
                            text = "По вашему запросу ничего не найдено",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.medium,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }
        }

    }
}