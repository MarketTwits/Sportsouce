package com.markettwits.sportsouce.shop.search.presentation.screen

import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.search.presentation.component.ShopSearchComponent
import com.markettwits.sportsouce.shop.search.presentation.components.inner.ShopSearchBarInner
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore

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
            ).background(SauceTheme.colors.background)
        ) {
        }
    }

    }
