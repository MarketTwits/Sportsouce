package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.paging.fold
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.FontNunito
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "По вашему запросу не были найдены результаты",
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 16.sp,
                    )
                }
            }
        )
    }
}
