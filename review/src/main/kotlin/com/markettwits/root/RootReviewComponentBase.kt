package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.di.ComponentKoinContext
import com.markettwits.di.newsModule
import com.markettwits.review.di.reviewModule
import com.markettwits.review.presentation.ReviewComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase

class RootReviewComponentBase(context: ComponentContext) : RootReviewComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootReviewComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(newsModule, reviewModule, sportSouceNetworkModule)
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
                    }
                ),
                newsComponent = newsComponent(componentContext)
            )
            is RootReviewComponent.Config.Start -> RootReviewComponent.Child.Start(
                RootStartScreenComponentBase(componentContext, startId = config.startId, pop = navigation::pop)
            )
        }

    private fun newsComponent(componentContext: ComponentContext): RootNewsComponent =
        RootNewsComponentBase(context = componentContext)
}