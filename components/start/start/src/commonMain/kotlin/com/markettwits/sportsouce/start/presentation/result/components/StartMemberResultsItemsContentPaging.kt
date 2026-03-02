package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.paging.fold
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.start.presentation.common.components.StartEmptySearchResultCard
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@Composable
fun StartMemberResultsItemsContentPaging(
    modifier: Modifier = Modifier,
    totalCount: Int,
    items: LazyPagingItems<MemberResult>,
    onClickMemberResult: (MemberResult) -> Unit,
) {
    AdaptivePane {
        items.fold(
            onLoading = {
                StartMemberResultsShimmer(modifier = modifier)
            },
            onException = {
                it.mapToSauceError().SauceErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    onClickRetry = items::refresh
                )
            },
            onSuccess = {
                StartMemberResultsPagedList(
                    modifier = modifier,
                    totalCount = totalCount,
                    items = items,
                    onClickMemberResult = onClickMemberResult
                )
            },
            onEmpty = {
                StartEmptySearchResultCard(
                    title = "Результаты не найдены",
                    message = "По текущим фильтрам и поисковому запросу нет совпадений. Попробуйте изменить параметры поиска или очистить фильтры."
                )
            }
        )
    }
}
