package com.markettwits.root.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.ProfileScreenComponent
import com.markettwits.profile.presentation.sign_in.SignInScreenComponent
import com.markettwits.starts.DefaultStartsComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<*, RootComponent.Child>>
    fun navigate(configuration: RootComponent.Configuration)
    @Serializable
    sealed class Configuration {
        @Serializable
        object Starts : Configuration()
        @Serializable
        object News : Configuration()
        @Serializable
        object Profile : Configuration()
        @Serializable
        object Login : Configuration()
    }

    sealed class Child {
        data class Starts(val component: DefaultStartsComponent) : Child()
        data class Profile(val component: ProfileScreenComponent) : Child()
        data class Login(val component: SignInScreenComponent) : Child()
        object News : Child()
    }
}