package com.markettwits.news_event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.BackFloatingActionButton
import com.markettwits.core_ui.components.BaseDivider
import com.markettwits.core_ui.components.FullImageContent
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.news_list.domain.NewsInfo
import de.charlex.compose.material3.HtmlText

@Composable
fun NewsEventContent(modifier: Modifier = Modifier, news: NewsInfo, goBack : () -> Unit) {
    Box(modifier = modifier.fillMaxSize()){
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            FullImageContent(imageUrl = news.imageUrl)
            Column(modifier = modifier.padding(20.dp)) {
                Text(
                    modifier = modifier.padding(bottom = 10.dp),
                    text = news.title,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
                HtmlText(
                    text = news.fullDescription,
                    lineHeight = 15.sp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                BaseDivider(modifier.fillMaxWidth())
                Row(modifier = modifier.padding(vertical = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Дата добавления:",
                        fontSize = 12.sp,
                        fontFamily = FontNunito.bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = news.createData,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

            }
        }
        BackFloatingActionButton { goBack() }
    }

}