package com.markettwits.sportsouce.review.review.presentation.components.content

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.review.review.presentation.components.albums.ReviewPhotoAlbums
import com.markettwits.sportsouce.news.news_list.components.NewsContent
import com.markettwits.sportsouce.review.review.presentation.components.actual.ActualStarts
import com.markettwits.sportsouce.review.review.presentation.components.actual.ActualStartsShimmer
import com.markettwits.sportsouce.review.review.presentation.components.archive.ArchiveStarts
import com.markettwits.sportsouce.review.review.presentation.components.products.SalesProductsContent
import com.markettwits.sportsouce.review.review.presentation.components.review_menu.ReviewMenu
import com.markettwits.sportsouce.review.review.presentation.components.social_network.SocialNetwork
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun ReviewContent(
    news: List<NewsItem>,
    actual: List<StartsListItem>,
    archive: List<StartsListItem>,
    products: List<ShopItem>,
    merchProducts: List<ShopItem>,
    onClickStart: (StartsListItem) -> Unit,
    onClickNewsInfo: (NewsItem) -> Unit,
    onClickMenu: (Int) -> Unit,
    onClickProduct: (ShopItem) -> Unit,
    onClickShowMoreProducts: () -> Unit,
    onClickShowMoreMerchProducts: () -> Unit,
    onClickTelegram: () -> Unit,
    onClickVk: () -> Unit,
    isLoading: Boolean = false,
    photoAlbums: List<ReviewPhotoAlbum> = emptyList(),
    photoAlbumsLoading: Boolean = false,
    photoAlbumsFailed: Boolean = false,
    onClickPhotoAlbum: (Int) -> Unit = {},
    onRetryPhotoAlbums: () -> Unit = {},
    notification: @Composable ((Modifier) -> Unit),
) {
    ReviewSection(maxWidth = 1160.dp) {
        if (news.isNotEmpty() || isLoading) {
            NewsContent(items = news, isLoading = isLoading && news.isEmpty()) {
                onClickNewsInfo(it)
            }
        }
        ReviewMenu {
            onClickMenu(it)
        }
        notification(Modifier)
    }

    if (actual.isEmpty() && isLoading) {
        ReviewSection(maxWidth = 1160.dp) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ActualStartsShimmer()
        }
    }

    if (actual.isNotEmpty()) {
        ReviewSection(maxWidth = 1160.dp) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ActualStarts(starts = actual) {
                onClickStart(it)
            }
        }
    }
    if (photoAlbums.isNotEmpty() || photoAlbumsLoading || photoAlbumsFailed) {
        ReviewSection(maxWidth = 1160.dp) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ReviewPhotoAlbums(photoAlbums, photoAlbumsLoading, photoAlbumsFailed,
                onClickPhotoAlbum, { onClickMenu(3) }, onRetryPhotoAlbums)
        }
    }
    if (actual.isNotEmpty()) {
        if (products.isNotEmpty()) {
            ReviewSection(maxWidth = 1920.dp) {
                HorizontalDivider(modifier = Modifier.padding(10.dp))
                SalesProductsContent(
                    title = "Акции",
                    items = products,
                    onClickItem = onClickProduct,
                    onClickShowMoreProducts = onClickShowMoreProducts
                )
            }
        }
        ReviewSection(maxWidth = 1160.dp) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            SocialNetwork(onClickVk = onClickVk, onClickTelegram = onClickTelegram)
        }
        ReviewSection(maxWidth = 1160.dp) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ArchiveStarts(starts = archive) {
                onClickStart(it)
            }
        }
        if (merchProducts.isNotEmpty()) {
            ReviewSection(maxWidth = 1920.dp) {
                HorizontalDivider(modifier = Modifier.padding(10.dp))
                SalesProductsContent(
                    title = "Мерч",
                    items = merchProducts,
                    onClickItem = onClickProduct,
                    onClickShowMoreProducts = onClickShowMoreMerchProducts
                )
            }
        }
    }
}

@Composable
private fun ReviewSection(
    maxWidth: Dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth),
            content = content
        )
    }
}
