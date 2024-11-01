package com.markettwits.shop.order.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class CoroutineComponent(
    componentContext: ComponentContext,
    protected val scope: CoroutineScope
) : ComponentContext by componentContext {
    
    init {
        lifecycle.doOnDestroy {
            scope.cancel("coroutine scope on destory")
        }
    }
}
