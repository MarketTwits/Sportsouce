package com.markettwits.sportsouce.news.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.news.di.newsModule
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponentBase
import com.markettwits.sportsouce.news.news_event.store.NewsEventStoreFactory
import com.markettwits.sportsouce.news.news_list.component.NewsComponentBase

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
                    onBack = navigation::pop
                )
            )
        }
}