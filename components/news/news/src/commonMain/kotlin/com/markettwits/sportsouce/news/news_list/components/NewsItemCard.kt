package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.news.common.model.NewsItem

@Composable
fun NewsItemCard(
    modifier: Modifier = Modifier,
    newsInfo: NewsItem,
    cardWidth: Dp = 270.dp,
    cardHeight: Dp = 240.dp,
    onClick: (NewsItem) -> Unit,
) {
    NewsCardLayout(
        modifier = modifier.clickable(role = Role.Button) { onClick(newsInfo) },
        cardWidth = cardWidth,
        cardHeight = cardHeight,
        image = {
            SubcomposeAsyncImage(
                model = newsInfo.imageUrl,
                contentDescription = newsInfo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(Shapes.medium),
                error = {
                    SubcomposeAsyncImageContent(
                        painter = DefaultImages.EmptyImageStart(),
                        contentScale = ContentScale.Crop
                    )
                },
                success = {
                    SubcomposeAsyncImageContent()
                },
                loading = {
                    SauceSkeleton(Modifier.fillMaxSize(), Shapes.medium)
                }
            )
        },
    ) {
        Text(
            overflow = TextOverflow.Ellipsis,
            text = newsInfo.title,
            maxLines = 1,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = FontNunito.bold(),
            color = SauceTheme.colors.textPrimary,
        )
        HtmlText(
            modifier = Modifier.heightIn(min = with(LocalDensity.current) { 32.sp.toDp() }),
            text = newsInfo.fullDescription,
            lineHeight = 16.sp,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.regular(),
            color = SauceTheme.colors.textSecondary,
        )
    }
}

@Composable
internal fun NewsItemCardShimmer(cardWidth: Dp, cardHeight: Dp) {
    val titleHeight = with(LocalDensity.current) { 20.sp.toDp() }
    val lineHeight = with(LocalDensity.current) { 16.sp.toDp() }
    NewsCardLayout(cardWidth = cardWidth, cardHeight = cardHeight,
        image = { SauceSkeleton(Modifier.fillMaxSize(), Shapes.medium) }) {
        Box(Modifier.fillMaxWidth().height(titleHeight)) {
            SauceSkeleton(Modifier.fillMaxWidth(0.65f).height(titleHeight * 0.8f))
        }
        Column(Modifier.fillMaxWidth().height(lineHeight * 2), verticalArrangement = Arrangement.SpaceEvenly) {
            SauceSkeleton(Modifier.fillMaxWidth(0.95f).height(lineHeight * 0.65f))
            SauceSkeleton(Modifier.fillMaxWidth(0.7f).height(lineHeight * 0.65f))
        }
    }
}

/** The image stays stable; text grows with font scale instead of being clipped by card height. */
@Composable
private fun NewsCardLayout(
    modifier: Modifier = Modifier,
    cardWidth: Dp,
    cardHeight: Dp,
    image: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.width(cardWidth)) {
        Box(Modifier.fillMaxWidth().height(cardHeight * (2f / 3f)), content = image)
        Column(Modifier.fillMaxWidth().padding(top = 8.dp), content = content)
    }
}
