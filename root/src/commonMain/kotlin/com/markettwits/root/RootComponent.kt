package com.markettwits.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.random_user.RandomUser
import detail.presentation.UserDetailComponent
import presentation.component.ListComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>
    fun navigate(configuration: Configuration)
    @Serializable
    sealed interface Configuration {
        @Serializable
        object List : Configuration
        @Serializable
        data class Detail(val item : RandomUser) : Configuration
    }

    sealed interface Child {
        data class List(val component: ListComponent) : Child
        data class Detail(val component: UserDetailComponent) : Child

    }
}