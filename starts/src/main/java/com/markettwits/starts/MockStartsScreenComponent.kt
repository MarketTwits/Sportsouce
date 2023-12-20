package com.markettwits.starts

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MockStartsScreenComponent() : StartsScreen {
    override fun onItemClick(startId: Int) = Unit
    override val starts: Value<StartsUiState> = MutableValue(
        StartsUiState.Success(
            listOf(
                listOf(
                    StartsListItem(
                        0,
                        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
                        "",
                        "17 декабря 2023 года",
                        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
                        "Лыжная база красное знамя у острова сокровищ возле сарая через 25 метров Колымская 25 дом 8",
                        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
                    )
                ),
                listOf(
                    StartsListItem(
                        0,
                        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
                        "",
                        "17 декабря 2023 года",
                        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
                        "Лыжная база красное знамя",
                        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
                    )
                ),
                listOf(
                    StartsListItem(
                        0,
                        "78-й Мемориал памяти Ф. Ивачёва посвященное памяти Новосибирских лыжных батальонов",
                        "",
                        "17 декабря 2023 года",
                        StartsListItem.StatusCode(2, "Регистрация на сайт скоро начнется"),
                        "Лыжная база красное знамя",
                        "<p><strong>Дистанция:</strong> 15 км, 30 км - классический стиль</p>"
                    )
                )
            )
        ),

    )
}