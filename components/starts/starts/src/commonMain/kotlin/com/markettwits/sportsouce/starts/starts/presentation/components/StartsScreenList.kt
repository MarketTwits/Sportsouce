package com.markettwits.sportsouce.starts.starts.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartCardV2
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun StartsScreenList(
    modifier: Modifier = Modifier,
    page: Int,
    state: StartsTabUiState,
    onClickItem: (StartsListItem) -> Unit,
    onClickRetry: () -> Unit,
    onLoadNext: () -> Unit,
) {
    val size = rememberScreenSizeInfo()
    val uriHandler = LocalUriHandler.current
    val gridState = rememberLazyGridState()

    ObservePagination(
        state = gridState,
        itemCount = state.items.size,
        enabled = state.items.isNotEmpty() && !state.isLoading && !state.isAppending && !state.endReached,
        onLoadNext = onLoadNext,
    )

    when {
        state.isLoading && state.items.isEmpty() -> {
            LoadingFullScreen()
        }

        state.error != null && state.items.isEmpty() -> {
            state.error.SauceErrorScreen(
                onClickRetry = onClickRetry
            )
        }

        state.items.isEmpty() -> {
            val emptyTitle = when (page) {
                0 -> "Здесь пока пусто"
                1 -> "Ближайших стартов пока нет"
                2 -> "Прошедших стартов пока нет"
                3 -> "Анонсов пока нет"
                else -> "Здесь пока пусто"
            }
            val emptyDescription = when (page) {
                0 -> "В данный момент в этой категории нет доступных стартов. Следите за обновлениями — новые мероприятия появятся совсем скоро!"
                1 -> "Сейчас нет запланированных стартов. Как только появятся новые мероприятия, мы сразу покажем их здесь. Следите за обновлениями!"
                2 -> "История стартов пока не содержит записей. Завершённые мероприятия будут отображаться в этом разделе"
                3 -> "В данный момент нет новых анонсов. Как только появится информация о предстоящих стартах, вы увидите её здесь"
                else -> "В данный момент нет доступных стартов. Следите за обновлениями!"
            }

            StartsEmptyCard(
                modifier = modifier,
                title = emptyTitle,
                description = emptyDescription
            )
        }

        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                state = gridState,
                columns = GridCells.Fixed(if (size.isPortrait()) 1 else 2),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Top
            ) {
                if (page == 1) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GradientInfoCard(
                            title = "Присоединяйтесь к нашему чату",
                            description = "Вступайте в наш Telegram-чат, чтобы всегда быть в курсе актуальных новостей по стартам и первыми узнавать о новых мероприятиях",
                            buttonText = "Перейти в чат",
                            color = SportSouceColor.SportSouceRegistryOpenGreen,
                            onButtonClick = {
                                uriHandler.openUri("https://t.me/sportsaucechat")
                            }
                        )
                    }
                }

                items(state.items, key = { it.id }) {
                    StartCardV2(
                        start = it,
                        onItemClick = onClickItem
                    )
                }

                if (state.isAppending) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingFullScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun ObservePagination(
    state: LazyGridState,
    itemCount: Int,
    enabled: Boolean,
    onLoadNext: () -> Unit,
) {
    LaunchedEffect(state, itemCount, enabled) {
        if (!enabled) return@LaunchedEffect

        snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex >= itemCount - 4) {
                    onLoadNext()
                }
            }
    }
}
