package com.markettwits.bottom_bar.component

import com.arkivanov.essenty.backhandler.BackCallback
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener


interface BottomBarSideEffectHandler {
    fun goBackInner()
}

abstract class BottomBarSideEffectHandlerAbstract(
    private val listener: BottomBarVisibilityListener
) : BottomBarSideEffectHandler {
    abstract val popInner: () -> Unit

    val bottomBarBackHandler: BackCallback = BackBottomBarCallback(
        listener = listener,
        goBack = { popInner() }
    )

    override fun goBackInner() {
        listener.show()
        popInner()
    }
}

fun BackBottomBarCallback(
    listener: BottomBarVisibilityListener, goBack: () -> Unit
): BackCallback = BackCallback(
    onBack = {
        listener.show()
        goBack()
    }, priority = Int.MAX_VALUE
)
