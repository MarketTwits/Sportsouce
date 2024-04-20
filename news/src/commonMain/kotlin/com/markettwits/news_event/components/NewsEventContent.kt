package com.markettwits.news_event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.FullImageContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.news_list.domain.NewsInfo

@Composable
fun NewsEventContent(modifier: Modifier = Modifier, news: NewsInfo, goBack: () -> Unit) {
    val window = rememberScreenSizeInfo()
    var isFullImage by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier.fillMaxSize()) {
        if (!window.isPortrait()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val imageWith = window.wDP.value / 2.5f
                Box(
                    modifier = Modifier
                        .width(imageWith.dp)
                        .fillMaxHeight()
                ) {
                    FullImageContent(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .clickable { isFullImage = true },
                        imageUrl = news.imageUrl,
                        isPortrait = false
                    )
                }
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .verticalScroll(rememberScrollState())
                ) {
                    NewsEventInnerContent(
                        modifier = modifier,
                        news = news
                    )
                }
            }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                FullImageContent(
                    modifier = Modifier.clickable { isFullImage = true },
                    imageUrl = news.imageUrl
                )
                NewsEventInnerContent(
                    modifier = modifier,
                    news = news
                )
            }
        }
        BackFloatingActionButton { goBack() }
    }
    if (isFullImage) {
        FullImageScreen(image = news.imageUrl) {
            isFullImage = false
        }
    }
}