package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.news.common.model.NewsItem

@Composable
fun NewsFeedItemCard(
    modifier: Modifier = Modifier,
    newsInfo: NewsItem,
    badgeText: String,
    onClick: (NewsItem) -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = Shapes.medium,
        onClick = { onClick(newsInfo) },
    ) {
        val badgeColor = resolveNewsCategoryColor(badgeText)
        val badgeTextColor = categoryBadgeTextColor(badgeColor)
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isCompact = maxWidth < 360.dp
            val isTiny = maxWidth < 220.dp
            val imageRatio = if (isTiny) 1.2f else 1.5f
            val titleFontSize = if (isTiny) 13.sp else if (isCompact) 14.sp else 15.sp
            val bodyFontSize = if (isTiny) 11.sp else 12.sp
            val badgeFontSize = if (isTiny) 12.sp else 13.sp

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    SubcomposeAsyncImage(
                        model = newsInfo.imageUrl.takeIf { it.isNotBlank() },
                        contentDescription = newsInfo.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(imageRatio),
                        loading = {
                            NewsImageShimmer()
                        },
                        error = {
                            Image(
                                painter = DefaultImages.EmptyImageStart(),
                                contentDescription = newsInfo.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        },
                        success = {
                            SubcomposeAsyncImageContent()
                        },
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(if (isCompact) 10.dp else 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(badgeColor.copy(alpha = 0.8f))
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                            text = badgeText,
                            color = badgeTextColor,
                            fontFamily = FontNunito.bold(),
                            fontSize = badgeFontSize,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = newsInfo.title,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold(),
                        fontSize = titleFontSize,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (newsInfo.fullDescription.isNotBlank()) {
                        HtmlText(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .heightIn(max = 35.dp),
                            text = newsInfo.fullDescription,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.regular(),
                            fontSize = bodyFontSize,
                            lineHeight = if (isTiny) 14.sp else 16.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    NewsCardTags(
                        modifier = Modifier.padding(top = 8.dp),
                        hashtags = newsInfo.hashtags
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = newsInfo.createData,
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.regular(),
                        fontSize = bodyFontSize
                    )
                }
            }
        }
    }
}
