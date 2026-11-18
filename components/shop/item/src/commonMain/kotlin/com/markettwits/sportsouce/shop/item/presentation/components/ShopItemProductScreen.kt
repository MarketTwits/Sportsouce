package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.ui.unit.dp
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemCompactPane
import com.markettwits.sportsouce.shop.item.presentation.components.panes.ShopItemLargePane
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlin.math.min

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ShopItemProductScreen(
    item: ShopItem,
    options: List<ShopExtraOptions>,
    isLoading: Boolean,
    error: SauceError?,
    similarProducts: Flow<PagingData<ShopItem>>,
    onClickOption: (String) -> Unit,
    onClickGoBack: () -> Unit,
    onRefresh: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    onClickItem: (ShopItem) -> Unit,
    cartContent: @Composable (Modifier) -> Unit,
) {

    val isLarge = calculateWindowSizeClass().isLarge
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        if (error != null) {
            val result = snackbar.showSnackbar(error.mapToString(), actionLabel = "Повторить", withDismissAction = true)
            if (result == SnackbarResult.ActionPerformed) onRefresh()
        }
    }

    AdaptivePane {
        PullToRefreshScreen(
            isRefreshing = isLoading,
            onRefresh = onRefresh,
        ) {
            Scaffold(
                snackbarHost = { SauceSnackbarHost(snackbar, tone = SauceSnackbarTone.Error) },
                bottomBar = {
                    if (!isLarge)
                        cartContent(Modifier.fillMaxWidth())
                },
                containerColor = SauceTheme.colors.background
            ) { paddingValues ->
                val similarItems = similarProducts.collectAsLazyPagingItems()
                val screenWidth = rememberScreenSizeInfo().wDP
                val maxColumns = 4
                val minColumnWidth = 180.dp
                val horizontalPadding = 16.dp
                val columns = remember(screenWidth) {
                    min(maxColumns, ((screenWidth - horizontalPadding) / minColumnWidth).toInt()).coerceAtLeast(1)
                }
                val safeTopPadding = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                val galleryTopPadding = safeTopPadding + SauceSpacing.small
                val listState = rememberLazyListState()
                val galleryBackdrop = rememberHazeState()
                val galleryHeightPx = with(LocalDensity.current) { (ShopItemPhotoHeight + galleryTopPadding).roundToPx() }
                val galleryBottom = remember(listState, galleryHeightPx, isLarge) {
                    {
                        if (!isLarge && listState.firstVisibleItemIndex == 0) {
                            galleryHeightPx - listState.firstVisibleItemScrollOffset
                        } else {
                            0
                        }
                    }
                }
                val galleryAtStatusBar by remember(galleryBottom) { derivedStateOf { galleryBottom() > 0 } }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    state = listState,
                    contentPadding = PaddingValues(top = if (isLarge) safeTopPadding else 0.dp),
                ) {
                    item {
                        if (isLarge) {
                            ShopItemLargePane(
                                item = item,
                                options = options,
                                cartContent = cartContent,
                                onClickOption = onClickOption
                            )
                        } else {
                            ShopItemCompactPane(
                                item = item,
                                options = options,
                                galleryTopPadding = galleryTopPadding,
                                galleryAtStatusBar = galleryAtStatusBar,
                                galleryModifier = Modifier.hazeSource(galleryBackdrop),
                                onClickOption = onClickOption
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    similarProductsItems(
                        items = similarItems,
                        columns = columns,
                        onClickItem = onClickItem
                    )

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                ShopItemActionRow(
                    modifier = Modifier.fillMaxWidth(),
                    galleryBackdrop = galleryBackdrop,
                    galleryBottom = galleryBottom,
                    onClickGoBack = onClickGoBack,
                    onClickShare = onClickShare
                )
            }
        }
    }
}
