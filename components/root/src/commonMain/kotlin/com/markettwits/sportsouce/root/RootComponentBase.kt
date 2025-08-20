package com.markettwits.sportsouce.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsauce.deeplink.model.Deeplink
import com.markettwits.sportsouce.bottom_bar.component.component.BottomBarComponentBase
import com.markettwits.sportsouce.bottom_bar.component.component.BottomBarComponentHandle
import com.markettwits.sportsouce.bottom_bar.di.bottomBarModule
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.review.root.RootReviewComponentBase
import com.markettwits.sportsouce.root_profile.RootProfileComponentBase
import com.markettwits.sportsouce.starts.root.RootStartsComponentBase

class RootComponentBase(
    componentContext: ComponentContext,
    private val deeplink: Deeplink.SportSauce? = null
) : ComponentContext by componentContext, RootComponent {

    private val scope = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }.getOrCreateKoinScope(listOf(bottomBarModule))

    private val stackNavigation = StackNavigation<RootComponent.Configuration>()

    private val slotNavigation = SlotNavigation<RootComponent.SlotConfiguration>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = RootComponent.Configuration.serializer(),
        initialStack = { getInitialStack() },
        childFactory = ::createChild,
    )

    private fun getInitialStack(): List<RootComponent.Configuration> {
        return when (deeplink) {
            is Deeplink.Starts.StartsList -> listOf(RootComponent.Configuration.Starts)
            is Deeplink.Starts.StartDetail -> listOf(RootComponent.Configuration.Starts)
            is Deeplink.News.NewsDetail -> listOf(RootComponent.Configuration.Review)
            is Deeplink.Clubs.ClubsList -> listOf(RootComponent.Configuration.Review)
            null -> listOf(RootComponent.Configuration.Review)
        }
    }

    override val slotChild: Value<ChildSlot<*, RootComponent.Navigation>> = childSlot(
        source = slotNavigation,
        serializer = RootComponent.SlotConfiguration.serializer(),
        initialConfiguration = { RootComponent.SlotConfiguration.BottomBar },
        childFactory = ::createBottomBar
    )

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.Starts -> RootComponent.Child.Starts(
                RootStartsComponentBase(
                    componentContext = componentContext,
                    deeplink = if (isInitialCreation()) deeplink as? Deeplink.Starts else null
                )
            )

            is RootComponent.Configuration.Profile -> RootComponent.Child.Profile(
                RootProfileComponentBase(
                    componentContext = componentContext
                )
            )

            is RootComponent.Configuration.Review -> RootComponent.Child.Review(
                RootReviewComponentBase(
                    context = componentContext,
                    newsDeeplink = if (isInitialCreation()) deeplink as? Deeplink.News else null,
                    clubsDeeplink = if (isInitialCreation()) deeplink as? Deeplink.Clubs else null
                )
            )
        }

    private fun isInitialCreation(): Boolean {
        return try {
            childStack.value.items.isEmpty()
        } catch (_: Exception) {
            true
        }
    }

    /**
     * Handle deeplink navigation without recreating the component
     * This approach ensures proper navigation flow without timing issues
     */
    fun handleDeeplink(deeplink: Deeplink.SportSauce) {
        val targetConfiguration = when (deeplink) {
            is Deeplink.Starts.StartsList -> RootComponent.Configuration.Starts
            is Deeplink.Starts.StartDetail -> RootComponent.Configuration.Starts
            is Deeplink.News.NewsDetail -> RootComponent.Configuration.Review
            is Deeplink.Clubs.ClubsList -> RootComponent.Configuration.Review
        }

        val currentConfiguration = childStack.value.active.configuration

        // Only navigate if we're not already on the target configuration
        if (currentConfiguration != targetConfiguration) {
            stackNavigation.bringToFront(targetConfiguration)
        }

        // Pass the deeplink to the appropriate child component after ensuring navigation
        // Use a slight delay to ensure the child component is properly initialized
        when (deeplink) {
            is Deeplink.Starts -> {
                val currentChild = childStack.value.active.instance
                if (currentChild is RootComponent.Child.Starts) {
                    currentChild.component.handleDeeplink(deeplink)
                }
            }

            is Deeplink.News -> {
                val currentChild = childStack.value.active.instance
                if (currentChild is RootComponent.Child.Review) {
                    currentChild.component.handleDeeplink(deeplink)
                }
            }

            is Deeplink.Clubs -> {
                val currentChild = childStack.value.active.instance
                if (currentChild is RootComponent.Child.Review) {
                    currentChild.component.handleDeeplink(deeplink)
                }
            }
        }
    }

    private fun createBottomBar(
        configuration: RootComponent.SlotConfiguration,
        componentContext: ComponentContext
    ): RootComponent.Navigation =
        when (configuration) {
            RootComponent.SlotConfiguration.BottomBar -> RootComponent.Navigation.BottomBar(
                BottomBarComponentBase(
                    componentContext = componentContext,
                    navigationComponentHandle = object : BottomBarComponentHandle {
                        override fun navigateTo(bottomBarConfiguration: BottomBarConfiguration) {
                            stackNavigation.bringToFront(
                                BottomBarConfigurationMapper.map(bottomBarConfiguration)
                            )
                        }

                        override fun getActiveConfiguration(observer: (BottomBarConfiguration) -> Unit) {
                            childStack.subscribe {
                                observer(
                                    BottomBarConfigurationMapper.map(it.active.configuration as RootComponent.Configuration)
                                )
                            }
                        }
                    },
                    bottomBarStorage = scope.get(),
                )
            )
        }
}