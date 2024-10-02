package com.markettwits.shop.search.presentation.screen

import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.shop.search.presentation.component.ShopSearchComponent
import com.markettwits.shop.search.presentation.components.inner.SearchFilterContent
import com.markettwits.shop.search.presentation.components.inner.ShopSearchBarInner
import com.markettwits.shop.search.presentation.store.ShopSearchStore

@Composable
fun ShopSearchScreen(modifier: Modifier = Modifier,component: ShopSearchComponent) {
    val state by component.state.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            ShopSearchBarInner(
                query = state.query,
                onQueryChanged = {
                     component.obtainEvent(ShopSearchStore.Intent.OnUpdateQuery(it))
                },
                onBrushClicked = {
                    component.obtainEvent(ShopSearchStore.Intent.OnClearQuery)
                },
                onClickBack = {
                    component.obtainEvent(ShopSearchStore.Intent.OnClickGoBack)
                },
                onApplyQuery = {
                    component.obtainEvent(ShopSearchStore.Intent.OnApplyQuery)
                }
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ).background(MaterialTheme.colorScheme.background)
        ) {

//            if (state.query.isEmpty() && state.filter.filterIsEmpty()) {
//                SearchHistoryColumn(
//                    items = state.searchHistory
//                ) {
//                    component.obtainEvent(StartsSearchStore.Intent.OnClickHistoryItem(it))
//                }
//            } else {
//                SearchResultColumn(
//                    starts = state.starts,
//                    onClickStart = { startId, startTitle ->
//                        component.obtainEvent(
//                            StartsSearchStore.Intent.OnClickStart(startId, startTitle)
//                        )
//                    }
//                )
//            }
        }
    }

    }
