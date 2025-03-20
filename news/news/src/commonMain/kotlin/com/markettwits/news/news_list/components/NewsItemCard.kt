package com.markettwits.news.news_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.news.common.model.NewsItem

@Composable
fun NewsItemCard(modifier: Modifier = Modifier, newsInfo: NewsItem, onClick: (NewsItem) -> Unit) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .size(width = 240.dp, height = 210.dp)
            .clip(Shapes.medium)
            .clickable { onClick(newsInfo) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f)
        ) {
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
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.25f)
                .padding(top = 8.dp)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = newsInfo.title,
                maxLines = 1,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.tertiary,
            )
            HtmlText(
                modifier = Modifier.heightIn(max = 30.dp),
                text = newsInfo.fullDescription,
                lineHeight = 14.sp,
                fontSize = 12.sp,
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}