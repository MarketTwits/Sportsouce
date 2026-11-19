package com.markettwits.sportsouce.review.review.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.markettwits.core.errors.api.composable.SauceErrorMediumContent
import com.markettwits.core_ui.items.image.rememberImageBackdropTint
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponent
import com.markettwits.sportsouce.review.review.presentation.components.content.ReviewContent
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import com.markettwits.sportsouce.start.search.search.presentation.components.publish.StartsSearchBarPublic

@Composable
fun ReviewScreen(
    component: ReviewComponent,
    notification: @Composable ((Modifier) -> Unit),
    toolbarActions: @Composable () -> Unit = {},
) {
    val state by component.value.collectAsState()
    val lifecycle = component.lifecycle
    var resumed by remember(lifecycle) { mutableStateOf(lifecycle.state == Lifecycle.State.RESUMED) }
    DisposableEffect(lifecycle) {
        val callbacks = object : Lifecycle.Callbacks {
            override fun onResume() { resumed = true }
            override fun onPause() { resumed = false }
        }
        lifecycle.subscribe(callbacks)
        onDispose { lifecycle.unsubscribe(callbacks) }
    }

    val colors = SauceTheme.colors
    val scroll = rememberScrollState()
    val toolbarState = rememberCollapsingToolbarScaffoldState()
    var newsHeight by remember { mutableIntStateOf(0) }
    var coverColor by remember(state.review.news.isEmpty()) { mutableStateOf(Color.Unspecified) }
    val tint by rememberImageBackdropTint(coverColor)
    val carouselVisibility by remember(scroll) {
        derivedStateOf {
            if (newsHeight == 0) 0f else (1f - scroll.value.toFloat() / newsHeight).coerceIn(0f, 1f)
        }
    }
    val glassTint = if (coverColor != Color.Unspecified || tint != colors.background) {
        lerp(colors.surface, tint, carouselVisibility)
    } else {
        colors.surface
    }
    val glassFadeDistance = with(LocalDensity.current) { 40.dp.toPx() }

    CollapsingToolbarScaffold(
        modifier = Modifier.drawBehind {
            if (carouselVisibility > 0f && tint != colors.background) {
                val toolbarBottom = (toolbarState.toolbarState.height + toolbarState.offsetY).coerceAtLeast(0)
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(lerp(colors.background, tint, carouselVisibility), colors.background),
                        startY = toolbarBottom.toFloat(),
                        endY = toolbarBottom + newsHeight * carouselVisibility,
                    )
                )
            } else {
                drawRect(colors.background)
            }
        },
        state = toolbarState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbarGlassColor = glassTint,
        toolbarGlassAlpha = (scroll.value / glassFadeDistance).coerceIn(0f, 1f),
        toolbar = {
            StartsSearchBarPublic(

                onClickSearchPanel = {
                    component.obtainEvent(ReviewStore.Intent.OnClickSearch)
                }, onClickSettings = {
                    component.obtainEvent(ReviewStore.Intent.OnClickSettings)
                }, actions = { toolbarActions() })
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                component.obtainEvent(ReviewStore.Intent.Launch(true))
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
            ) {
                ReviewContent(
                    isLoading = state.isLoading,
                    photoAlbums = state.photoAlbums,
                    photoAlbumsLoading = state.photoAlbumsLoading,
                    photoAlbumsFailed = state.photoAlbumsFailed,
                    onClickPhotoAlbum = { component.obtainEvent(ReviewStore.Intent.OnClickPhotoAlbum(it)) },
                    onRetryPhotoAlbums = { component.obtainEvent(ReviewStore.Intent.OnRetryPhotoAlbums) },
                    news = state.review.news,
                    newsModifier = Modifier.onSizeChanged { newsHeight = it.height },
                    newsAutoAdvance = resumed && carouselVisibility > 0.5f,
                    onNewsCoverColorChange = { coverColor = it },
                    actual = state.review.actualStarts,
                    archive = state.review.archiveStarts,
                    products = state.review.products ?: emptyList(),
                    merchProducts = state.review.merchProducts ?: emptyList(),
                    notification = notification,
                    onClickStart = {
                        component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
                    },
                    onClickNewsInfo = {
                        component.obtainEvent(ReviewStore.Intent.OnClickNews(it))
                    },
                    onClickMenu = {
                        component.obtainEvent(ReviewStore.Intent.OnClickMenu(it))
                    },
                    onClickProduct = {
                        component.obtainEvent(ReviewStore.Intent.OnClickProduct(it))
                    },
                    onClickTelegram = {
                        component.obtainEvent(ReviewStore.Intent.OnClickTelegram)
                    },
                    onClickShowMoreProducts = {
                        component.obtainEvent(ReviewStore.Intent.OnClickShowMoreSalesProducts)
                    },
                    onClickShowMoreMerchProducts = {
                        component.obtainEvent(ReviewStore.Intent.OnClickShowMoreMerchProducts)
                    },
                    onClickVk = {
                        component.obtainEvent(ReviewStore.Intent.OnClickVk)
                    }
                )
                state.error?.SauceErrorMediumContent(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    component.obtainEvent(ReviewStore.Intent.Launch(true))
                }
            }
        }
    }
}
