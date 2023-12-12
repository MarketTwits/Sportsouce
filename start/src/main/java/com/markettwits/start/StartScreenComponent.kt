package com.markettwits.start

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class StartScreenComponent(componentContext: ComponentContext, val startId: Int) :
    ComponentContext by componentContext, StartScreen {

    override val start: MutableValue<String> = MutableValue("")

    init {
        start.value = startId.toString()
    }
}