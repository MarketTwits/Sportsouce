package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.club.root.RootClubComponentBase
import com.markettwits.news.di.newsModule
import com.markettwits.news.news_event.component.NewsEventComponentBase
import com.markettwits.news.news_event.store.NewsEventStoreFactory
import com.markettwits.news.news_list.component.NewsComponentBase
import com.markettwits.popular.root.RootStartsPopularComponentBase
import com.markettwits.random.root.presentation.RootStartRandomComponentBase
import com.markettwits.review.di.reviewModule
import com.markettwits.review.presentation.component.ReviewComponentBase
import com.markettwits.root.di.reviewRootModule
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponentBase
import com.markettwits.selfupdater.components.notification.di.notificationModule
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponentBase
import com.markettwits.settings.root.RootSettingsComponentBase
import com.markettwits.shop.catalog.di.shopCatalogModule
import com.markettwits.shop.root.RootShopCatalogComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_search.root.RootStartsSearchComponentBase


class RootReviewComponentBase(context: ComponentContext) : RootReviewComponent,
    ComponentContext by context {

    private val navigation = StackNavigation<RootReviewComponent.Config>()
    private val slotNavigation = SlotNavigation<RootReviewComponent.ConfigSlot>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(
            reviewRootModule,
            newsModule,
            reviewModule,
            notificationModule,
            shopCatalogModule
        )
    )

    override val childStack: Value<ChildStack<*, RootReviewComponent.Child>> = childStack(
        source = navigation,
        serializer = RootReviewComponent.Config.serializer(),
        initialConfiguration = RootReviewComponent.Config.Review,
        handleBackButton = true,
        childFactory = ::child,
    )
    override val childSlot: Value<ChildSlot<RootReviewComponent.ConfigSlot, RootReviewComponent.ChildSlot>> =
        childSlot(
            source = slotNavigation,
            serializer = RootReviewComponent.ConfigSlot.serializer(),
            initialConfiguration = {
                RootReviewComponent.ConfigSlot.Notification
            },
            childFactory = ::slotChild
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
                        navigation.pushNew(RootReviewComponent.Config.Start(it))
                    },
                    onClickMenu = {
                        navigation.pushNew(handleMenu(it))
                    },
                    onClickSearch = {
                        navigation.pushNew(RootReviewComponent.Config.Search)
                    },
                    onClickNews = {
                        navigation.pushNew(RootReviewComponent.Config.NewsEvent(it))
                    },
                    onClickSettings = {
                        navigation.pushNew(RootReviewComponent.Config.Settings)
                    }
                ),
                newsComponent = NewsComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    onItemClick = {
                        navigation.pushNew(RootReviewComponent.Config.NewsEvent(it))
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

            is RootReviewComponent.Config.Random -> RootReviewComponent.Child.Random(
                RootStartRandomComponentBase(
                    context = componentContext,
                    dependencies = scope.get(),
                    pop = navigation::pop
                )
            )

//            is RootReviewComponent.Config.Schedule -> RootReviewComponent.Child.Schedule(
//                RootStartsScheduleComponentBase(
//                    context = componentContext,
//                    dependencies = scope.get(),
//                    pop = navigation::pop
//                )
//            )

            is RootReviewComponent.Config.Popular -> RootReviewComponent.Child.Popular(
                RootStartsPopularComponentBase(
                    context = componentContext,
                    pop = navigation::pop,
                )
            )

            is RootReviewComponent.Config.NewsEvent -> RootReviewComponent.Child.NewsEvent(
                NewsEventComponentBase(
                    context = componentContext,
                    item = config.news,
                    storeFactory = NewsEventStoreFactory(DefaultStoreFactory()),
                    onBack = navigation::pop
                )
            )

            is RootReviewComponent.Config.Search -> RootReviewComponent.Child.Search(
                RootStartsSearchComponentBase(
                    context = componentContext,
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Notification -> RootReviewComponent.Child.Notification(
                SelfUpdateComponentBase(
                    componentContext = componentContext,
                    newAppVersion = config.newAppVersion,
                    storeFactory = scope.get(),
                    goBack = navigation::pop
                )
            )

            is RootReviewComponent.Config.Settings -> RootReviewComponent.Child.Settings(
                RootSettingsComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Club -> RootReviewComponent.Child.Club(
                RootClubComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )

            is RootReviewComponent.Config.Shop -> RootReviewComponent.Child.Shop(
                RootShopCatalogComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )
        }

    private fun slotChild(
        configuration: RootReviewComponent.ConfigSlot,
        componentContext: ComponentContext,
    ): RootReviewComponent.ChildSlot = when (configuration) {

        is RootReviewComponent.ConfigSlot.Notification -> RootReviewComponent.ChildSlot.Notification(
            component = InAppNotificationComponentBase(
                componentContext = componentContext,
                notificationStorage = scope.get(),
                storeFactory = scope.get(),
                openFullScreen = {
                    navigation.pushNew(RootReviewComponent.Config.Notification(it))
                }
            ),
            render = scope.get()
        )
    }

    private fun handleMenu(itemId: Int): RootReviewComponent.Config {
        return when (itemId) {
            0 -> RootReviewComponent.Config.Popular
            // 1 -> RootReviewComponent.Config.Schedule
            2 -> RootReviewComponent.Config.Club
            3 -> RootReviewComponent.Config.Search
            4 -> RootReviewComponent.Config.Shop
            else -> RootReviewComponent.Config.Search
        }
    }
}