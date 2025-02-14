package com.markettwits.news_event.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.news_list.domain.NewsInfo

@Composable
internal fun NewsEventContent(modifier: Modifier = Modifier, news: NewsInfo, goBack: () -> Unit) {


    var isFullImage by remember { mutableStateOf(false) }

    if (isFullImage) {
        FullImageScreen(image = news.imageUrl) {
            isFullImage = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            BackFloatingActionButton { goBack() }
            Text(
                textAlign = TextAlign.Start,
                modifier = modifier.padding(bottom = 10.dp),
                text = news.title,
                fontSize = 24.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            NewsEventBadgeTags(
                hashtags = news.hashtags
            )
            Spacer(modifier = Modifier.padding(8.dp))
            NewsEventMainImage(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                imageUrl = news.imageUrl,
                onClickImage = {
                    isFullImage = true
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            HtmlText(
                text = news.fullDescription,
                lineHeight = 16.sp,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            NewsEventCreatedDate(
                createdDate = news.createData
            )
        }
    }
}