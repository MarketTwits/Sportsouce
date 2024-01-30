package com.markettwits.core_ui.coroutineComponent

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class CoroutineComponent(
    componentContext: ComponentContext,
    private val scope: CoroutineScope
) : ComponentContext by componentContext