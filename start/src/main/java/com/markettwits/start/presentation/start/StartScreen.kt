package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.Value

interface StartScreen {
    val start: Value<StartItemUi>
    fun goBack()
    fun goMembers()
}