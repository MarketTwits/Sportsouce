package com.markettwits.starts

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.StartScreenComponent
import ru.alexpanov.core_network.model.all.StartsCloud


interface StartsScreen {
    fun onItemClick(startdId : Int)
    val data : Value<StartsCloud>
    val starts : Value<StartsUiState>
}