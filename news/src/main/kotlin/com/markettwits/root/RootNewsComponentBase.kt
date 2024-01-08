package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.news_event.NewsEventComponentBase
import com.markettwits.news_list.data.NewsDataSourceBase
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.news_list.presentation.NewsComponentBase
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class RootNewsComponentBase(context: ComponentContext) : RootNewsComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootNewsComponent.Config>()

    override val childStack: Value<ChildStack<*, RootNewsComponent.Child>> = childStack(
        source = navigation,
        serializer = RootNewsComponent.Config.serializer(),
        initialConfiguration = RootNewsComponent.Config.News,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootNewsComponent.Config,
        componentContext: ComponentContext,
    ): RootNewsComponent.Child =
        when (config) {
            is RootNewsComponent.Config.News -> RootNewsComponent.Child.News(
                NewsComponentBase(
                    context = componentContext,
                    storeFactory = NewsStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        dataSource = NewsDataSourceBase(
                            StartsRemoteDataSourceImpl(
                                HttpClientProvider2(
                                    JsonProvider().get(),
                                    "https://sport-73zoq.ondigitalocean.app"
                                )
                            ),
                            mapper = NewsRemoteToDomainMapper.Base(BaseTimeMapper()),
                        )
                    ),
                    onItemClick = {
                        navigation.push(RootNewsComponent.Config.NewsEvent(it))
                    }
                )
            )
            is RootNewsComponent.Config.NewsEvent -> RootNewsComponent.Child.NewsEvent(
                NewsEventComponentBase(
                    context = componentContext,
                    item = config.news,
                    storeFactory = NewsEventStoreFactory(DefaultStoreFactory()),
                    pop = navigation::pop
                )
            )
        }
}