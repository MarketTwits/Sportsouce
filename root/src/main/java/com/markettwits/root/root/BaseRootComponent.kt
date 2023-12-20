package com.markettwits.root.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.markettwits.data.store.AuthCacheDataSource
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.presentation.ProfileScreenComponent
import com.markettwits.profile.presentation.sign_in.SignInScreenComponent
import com.markettwits.start.core.BaseTimeMapper
import com.markettwits.starts.DefaultStartsComponent
import com.markettwits.starts.data.BaseStartsDataSource
import com.markettwits.starts.data.StartsCloudToUiMapper
import kotlinx.serialization.Serializable
import ru.alexpanov.core_network.api.StartsRemoteDataSourceImpl
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class BaseRootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {
    private val navigation = StackNavigation<RootComponent.Configuration>()

    private val stack =
        childStack(
            source = navigation,
            serializer = RootComponent.Configuration.serializer(),
            initialStack = { listOf(RootComponent.Configuration.Login) },
            childFactory = ::createChild,
        )
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = stack
    override fun navigate(configuration: RootComponent.Configuration) {
        navigation.bringToFront(configuration)
    }

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.Starts -> RootComponent.Child.Starts(
                DefaultStartsComponent(
                    componentContext = componentContext,
                )
            )
            is RootComponent.Configuration.News -> RootComponent.Child.News
            is RootComponent.Configuration.Profile -> RootComponent.Child.Profile(
                ProfileScreenComponent()
            )
            is RootComponent.Configuration.Login -> RootComponent.Child.Login(
                SignInScreenComponent(BaseAuthDataSource(
                    remoteService =
                        StartsRemoteDataSourceImpl(HttpClientProvider2(JsonProvider().get())),
                    local = AuthCacheDataSource(RealmDatabaseProvider.Base())
                ))
            )
        }
}