package com.markettwits.starts

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.start.StartScreenComponent
import ru.alexpanov.core_network.model.all.StartsCloud

class MockStartsScreenComponent() : StartsScreen {
    override fun onItemClick(startdId: Int) = Unit
    override fun onPageClick() = Unit

    override val data: Value<StartsCloud> = MutableValue(StartsCloud(0, emptyList()))
    override val starts: Value<StartsUiState> = MutableValue(
        StartsUiState.Success(
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
    )
}