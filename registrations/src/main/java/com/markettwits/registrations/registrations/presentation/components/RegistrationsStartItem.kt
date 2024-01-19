package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import eu.bambooapps.material3.pullrefresh.PullRefreshDefaults
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorColors
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationsStart(
    starts: List<RegistrationsStore.StartsStateInfo>,
    isRefreshing: Boolean,
    onClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        onRefresh()
    })
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.pullRefresh(state)) {
            items(starts) { event ->
                RegistrationsCard(event) {
                    onClick(it)
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing, state = state,
            colors = PullRefreshIndicatorDefaults.colors(
                containerColor = Color.White,
                contentColor = SportSouceColor.SportSouceBlue
            ),
            shadowElevation = 5.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}