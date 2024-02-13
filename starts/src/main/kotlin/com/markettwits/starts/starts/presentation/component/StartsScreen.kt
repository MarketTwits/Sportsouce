package com.markettwits.starts.starts.presentation.component

import com.arkivanov.decompose.value.Value


interface StartsScreen {
    fun onItemClick(startId : Int)
    fun onSearchClick()
    fun retry()
    val starts : Value<StartsUiState>
}