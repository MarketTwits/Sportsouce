package com.markettwits.sportsouce.news.news_event.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponent
import com.markettwits.sportsouce.news.news_event.components.NewsEventContent
import com.markettwits.sportsouce.news.news_event.store.NewsEventStore

@Composable
fun NewsEventScreen(component: NewsEventComponent) {
    val state by component.state.collectAsState()

    // Show content when news is available
    state.news?.let { news ->
        NewsEventContent(news = news, goBack = {
            component.obtainEvent(NewsEventStore.Intent.Pop)
        })
    }

    // Show loading screen when loading and no data
    if (state.isLoading && state.news == null) {
        LoadingFullScreen(
            onClickBack = {
                component.obtainEvent(NewsEventStore.Intent.Pop)
            }
        )
    }

    // Show error screen when there's an error
    state.error?.let { error ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ошибка загрузки новости",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = error.message ?: "Произошла неизвестная ошибка",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    component.obtainEvent(NewsEventStore.Intent.Retry)
                }
            ) {
                Text("Повторить")
            }
        }
    }
}