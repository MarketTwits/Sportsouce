package com.markettwits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.presentation.components.actual.ActualStarts
import com.markettwits.presentation.components.archive.ArchiveStarts
import com.markettwits.presentation.components.review_menu.ReviewMenu
import com.markettwits.presentation.components.social_network.SocialNetwork
import com.markettwits.root.RootNewsComponent
import com.markettwits.root.RootNewsScreen
import com.markettwits.starts.StartsListItem

@Composable
fun ReviewScreen(component: ReviewComponent, newsComponent: RootNewsComponent) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(10.dp)
            .fillMaxSize()
    ) {
        RootNewsScreen(newsComponent)
        ReviewMenu()
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        ActualStarts(starts = fakeList){

        }
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        ArchiveStarts(starts = fakeList){

        }
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        SocialNetwork()
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