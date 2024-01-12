package com.markettwits.random.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.random.presentation.components.actual.ActualStarts
import com.markettwits.random.presentation.components.archive.ArchiveStarts
import com.markettwits.random.presentation.components.review_menu.ReviewMenu
import com.markettwits.random.presentation.components.social_network.SocialNetwork
import com.markettwits.random.presentation.store.ReviewStore
import com.markettwits.root.RootNewsComponent
import com.markettwits.root.RootNewsScreen
import com.markettwits.starts.StartsListItem

@Composable
fun ReviewScreen(component: ReviewComponent, newsComponent: RootNewsComponent) {
    val state by component.value.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(10.dp)
    ) {
        if (state.actualStarts.isNotEmpty()){
            RootNewsScreen(newsComponent)
        }
        ReviewMenu()
        if (state.actualStarts.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ActualStarts(starts = state.actualStarts) {
                component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
            }
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            ArchiveStarts(starts = state.archiveStarts) {
                component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
            }
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            SocialNetwork()
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = SportSouceColor.SportSouceBlue
            )
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = {
                },
                onClickRetry = {
                    component.obtainEvent(ReviewStore.Intent.Launch)
                }
            )
        }
    }
}

private val fakeList = listOf(
    StartsListItem(
        0,
        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
        "",
        "17 декабря 2023 года",
        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
        "Лыжная база красное знамя у острова сокровищ возле сарая через 25 метров Колымская 25 дом 8",
        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
    ),
    StartsListItem(
        1,
        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
        "",
        "17 декабря 2023 года",
        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
        "Лыжная база красное знамя",
        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
    ),
    StartsListItem(
        2,
        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
        "",
        "17 декабря 2023 года",
        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
        "Лыжная база красное знамя",
        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
    ),
    StartsListItem(
        4,
        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
        "",
        "17 декабря 2023 года",
        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
        "Лыжная база красное знамя",
        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
    ),
    StartsListItem(
        4,
        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
        "",
        "17 декабря 2023 года",
        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
        "Лыжная база красное знамя",
        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
    )
)