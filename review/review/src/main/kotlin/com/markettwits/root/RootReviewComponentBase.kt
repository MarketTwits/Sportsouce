package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.di.ComponentKoinContext
import com.markettwits.di.newsModule
import com.markettwits.news_event.NewsEventComponentBase
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.data.NewsDataSource
import com.markettwits.news_list.data.NewsDataSourceBase
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.news_list.presentation.NewsComponentBase
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import com.markettwits.popular.presentation.PopularStartsComponentBase
import com.markettwits.random.root.presentation.RootStartRandomComponentBase
import com.markettwits.review.di.reviewModule
import com.markettwits.review.presentation.ReviewComponentBase
import com.markettwits.root.di.rootModule
import com.markettwits.schedule.root.RootStartsScheduleComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_filter.root.RootStartFilterComponentBase


class RootReviewComponentBase(context: ComponentContext) : RootReviewComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootReviewComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootModule, newsModule, reviewModule, sportSouceNetworkModule)
    )

    override val childStack: Value<ChildStack<*, RootReviewComponent.Child>> = childStack(
        source = navigation,
        serializer = RootReviewComponent.Config.serializer(),
        initialConfiguration = RootReviewComponent.Config.Review,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootReviewComponent.Config,
        componentContext: ComponentContext,
    ): RootReviewComponent.Child =
        when (config) {
            is RootReviewComponent.Config.Review -> RootReviewComponent.Child.Review(
                component = ReviewComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    onStartClick = {
                        navigation.push(RootReviewComponent.Config.Start(it))
                    },
                    onClickMenu = {
                        navigation.push(handleMenu(it))
                    }
                ),
                newsComponent = NewsComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    onItemClick = {
                        navigation.push(RootReviewComponent.Config.NewsEvent(it))
                    }
                )
            )

            is RootReviewComponent.Config.Start -> RootReviewComponent.Child.Start(
                RootStartScreenComponentBase(
                    componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Filter -> RootReviewComponent.Child.Filter(
                RootStartFilterComponentBase(
                    context = componentContext,
                    dependencies = scope.get(),
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Random -> RootReviewComponent.Child.Random(
                RootStartRandomComponentBase(
                    context = componentContext,
                    dependencies = scope.get(),
                    pop = navigation::pop

                )
            )

            is RootReviewComponent.Config.Schedule -> RootReviewComponent.Child.Schedule(
                RootStartsScheduleComponentBase(
                    context = componentContext,
                    dependencies = scope.get(),
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Popular -> RootReviewComponent.Child.Popular(
                PopularStartsComponentBase(context = componentContext)
            )

            is RootReviewComponent.Config.NewsEvent -> RootReviewComponent.Child.NewsEvent(
                NewsEventComponentBase(
                    context = componentContext,
                    item = config.news,
                    storeFactory = NewsEventStoreFactory(DefaultStoreFactory()),
                    pop = navigation::pop
                )
            )
        }

    private fun handleMenu(itemId: Int): RootReviewComponent.Config {
        return when (itemId) {
            0 -> RootReviewComponent.Config.Popular
            1 -> RootReviewComponent.Config.Schedule
            2 -> RootReviewComponent.Config.Random
            3 -> RootReviewComponent.Config.Filter
            else -> RootReviewComponent.Config.Filter
        }
    }
}