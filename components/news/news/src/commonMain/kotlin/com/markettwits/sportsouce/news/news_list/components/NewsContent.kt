package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.news.common.model.NewsItem
import kotlinx.coroutines.flow.first

@Composable
fun NewsContent(
    modifier: Modifier = Modifier,
    items: List<NewsItem>,
    isLoading: Boolean = false,
    autoAdvance: Boolean = true,
    onActiveCoverColorChange: (Color) -> Unit = {},
    onClickItem: (NewsItem) -> Unit
) {
    val banners = remember(items) { items.distinctBy { it.id }.take(10) }
    val pager = rememberPagerState { banners.size }
    val dragged by pager.interactionSource.collectIsDraggedAsState()
    val settledPage = pager.settledPage
    val activeBanner = banners.getOrNull(settledPage)
    val coverColors = remember { mutableStateMapOf<String, Color>() }
    val activeColor = coverColors[activeBanner?.imageUrl] ?: Color.Unspecified
    val progress = remember(activeBanner?.id) { mutableFloatStateOf(0f) }

    LaunchedEffect(banners) {
        val urls = banners.mapTo(mutableSetOf()) { it.imageUrl }
        coverColors.keys.retainAll(urls)
    }
    LaunchedEffect(activeColor) { onActiveCoverColorChange(activeColor) }
    LaunchedEffect(autoAdvance, dragged, settledPage, activeBanner?.id, banners.size) {
        progress.floatValue = 0f
        if (!autoAdvance || dragged || banners.size < 2 || activeBanner == null) return@LaunchedEffect
        snapshotFlow { pager.isScrollInProgress }.first { !it }
        val startedAt = withFrameNanos { it }
        while (progress.floatValue < 1f) {
            progress.floatValue = withFrameNanos {
                ((it - startedAt) / NewsIntervalNanos).coerceIn(0f, 1f)
            }
        }
        snapshotFlow { pager.isScrollInProgress }.first { !it }
        if (pager.settledPage == settledPage) {
            pager.animateScrollToPage((settledPage + 1) % banners.size)
        }
    }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val cardWidth = (maxWidth * 0.88f).coerceAtMost(560.dp)
        val sidePadding = 16.dp.coerceAtMost((maxWidth - cardWidth) / 2)
        val pageSpacing = 16.dp
        if (isLoading && banners.isEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().semantics {
                    contentDescription = "Загрузка новостей"
                    progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
                },
                contentPadding = PaddingValues(start = sidePadding, end = sidePadding, top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(pageSpacing),
                userScrollEnabled = false,
            ) {
                items(3, key = { "loading-news-$it" }) {
                    NewsItemCardShimmer(cardWidth = cardWidth)
                }
            }
        } else if (banners.isNotEmpty()) {
            val colors = SauceTheme.colors
            val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
            HorizontalPager(
                state = pager,
                modifier = Modifier.fillMaxWidth(),
                pageSize = PageSize.Fixed(cardWidth),
                pageSpacing = pageSpacing,
                contentPadding = PaddingValues(
                    start = sidePadding,
                    end = maxWidth - cardWidth - sidePadding,
                    top = 12.dp,
                    bottom = 8.dp,
                ),
                beyondViewportPageCount = 1,
                key = { banners[it].id },
            ) { page ->
                val item = banners[page]
                NewsItemCard(
                    newsInfo = item,
                    cardWidth = cardWidth,
                    onCoverColor = { coverColors[item.imageUrl] = it },
                    coverOverlay = {
                        if (banners.size > 1) {
                            val cover = (coverColors[item.imageUrl] ?: Color.Unspecified).takeOrElse { colors.action }
                            val track = lerp(cover, Color.Black, 0.58f).copy(alpha = 0.74f)
                            val fill = lerp(cover, Color.White, 0.52f)
                            Box(
                                Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(4.dp).drawBehind {
                                    drawRect(track)
                                    if (page == settledPage) {
                                        val width = size.width * progress.floatValue
                                        drawRect(
                                            color = fill,
                                            topLeft = Offset(if (rtl) size.width - width else 0f, 0f),
                                            size = Size(width, size.height),
                                        )
                                    }
                                },
                            )
                        }
                    },
                    onClick = onClickItem,
                )
            }
        }
    }
}

private const val NewsIntervalNanos = 6_500_000_000f
