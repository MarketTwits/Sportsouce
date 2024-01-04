package com.markettwits.starts

import com.arkivanov.decompose.value.Value


interface StartsScreen {
    fun onItemClick(startId : Int)
    fun retry()
    val starts : Value<StartsUiState>
}