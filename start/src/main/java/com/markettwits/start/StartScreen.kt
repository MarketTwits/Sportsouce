package com.markettwits.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.start.StartRemote

interface StartScreen {
    val start: Value<StartRemote>
}