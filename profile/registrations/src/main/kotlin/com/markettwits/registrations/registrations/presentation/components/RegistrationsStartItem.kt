package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.refresh.PullToRefreshScreen
import com.markettwits.registrations.registrations.domain.StartsStateInfo


@Composable
fun RegistrationsStart(
    starts: List<StartsStateInfo>,
    isRefreshing: Boolean,
    onClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh::invoke,
    ) {
        LazyColumn(modifier = it) {
            items(starts) { event ->
                RegistrationsCard(event) { id ->
                    onClick(id)
                }
            }
        }
    }
}