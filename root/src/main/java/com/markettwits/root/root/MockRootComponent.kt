package com.markettwits.root.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MockRootComponent : RootComponent{
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = TODO()
    override fun navigate(configuration: RootComponent.Configuration) = Unit
}