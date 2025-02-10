package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.di.newsModule
import com.markettwits.news_event.component.NewsEventComponentBase
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.presentation.NewsComponentBase

class RootNewsComponentBase(context: ComponentContext) : RootNewsComponent,
    ComponentContext by context {
    private val koinContext = ComponentKoinContext()

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(newsModule)
    )
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
                    storeFactory = scope.get(),
                    onItemClick = {
                        navigation.pushNew(RootNewsComponent.Config.NewsEvent(it))
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