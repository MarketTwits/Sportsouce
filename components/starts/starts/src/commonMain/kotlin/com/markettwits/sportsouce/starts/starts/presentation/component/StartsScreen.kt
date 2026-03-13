package com.markettwits.sportsouce.starts.starts.presentation.component

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.starts.common.domain.StartsListItem


interface StartsScreen {
    fun onItemClick(startItem: StartsListItem)
    fun onSearchClick()
    fun onSettingsClick()
    fun retry(page: Int)
    fun onPageSelected(page: Int)
    fun onLoadNext(page: Int)
    val starts: Value<StartsUiState>
}
