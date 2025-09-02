package com.markettwits.sportsouce.starts.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsauce.deeplink.model.Deeplink
import com.markettwits.sportsouce.settings.root.RootSettingsComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponentBase
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.starts.di.startsModule
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsScreenComponent

class RootStartsComponentBase(
    private val componentContext: ComponentContext,
    private val deeplink: Deeplink.Starts? = null
) : RootStartsComponent, ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startsModule)
    )

    private val navigation = StackNavigation<RootStartsComponent.Config>()

    override val configStack = childStack(
        source = navigation,
        serializer = RootStartsComponent.Config.serializer(),
        initialStack = { getInitialStack() },
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun getInitialStack(): List<RootStartsComponent.Config> {
        return when (deeplink) {
            is Deeplink.Starts.StartDetail -> {
                val startId = when {
                    deeplink.startId != null -> deeplink.startId.toString()
                    deeplink.startSlug != null -> deeplink.startSlug
                    else -> null
                }
                if (startId != null) {
                    // Create navigation stack: Starts -> Start(startId)
                    listOf(
                        RootStartsComponent.Config.Starts,
                        RootStartsComponent.Config.Start(null, startId)
                    )
                } else {
                    listOf(RootStartsComponent.Config.Starts)
                }
            }

            is Deeplink.Starts.StartsList -> listOf(RootStartsComponent.Config.Starts)
            null -> listOf(RootStartsComponent.Config.Starts)
        }
    }


    override fun handleDeeplink(deeplink: Deeplink.Starts) {
        when (deeplink) {
            is Deeplink.Starts.StartDetail -> {
                val startId = when {
                    deeplink.startId != null -> deeplink.startId.toString()
                    deeplink.startSlug != null -> deeplink.startSlug
                    else -> null
                }
                if (startId != null) {
                    val targetConfig = RootStartsComponent.Config.Start(null, startId)
                    // Check if we already have this configuration to avoid duplicates
                    val existingConfig = configStack.value.items.find { it.configuration == targetConfig }
                    if (existingConfig != null) {
                        navigation.bringToFront(targetConfig)
                    } else {
                        // Ensure we have the starts list in the stack, then navigate to the detail
                        val currentStack = configStack.value.items.map { it.configuration }
                        if (!currentStack.contains(RootStartsComponent.Config.Starts)) {
                            // If starts list is not in stack, create proper navigation stack
                            navigation.replaceAll(
                                RootStartsComponent.Config.Starts,
                                targetConfig
                            )
                        } else {
                            // Starts list is already in stack, just navigate to detail
                            navigation.pushNew(targetConfig)
                        }
                    }
                }
            }

            is Deeplink.Starts.StartsList -> {
                // Navigate to starts list, or bring it to front if it already exists
                val targetConfig = RootStartsComponent.Config.Starts
                val existingConfig = configStack.value.items.find { it.configuration == targetConfig }
                if (existingConfig != null) {
                    navigation.bringToFront(targetConfig)
                } else {
                    navigation.pushNew(targetConfig)
                }
            }
        }
    }

    private fun createStartScreenInput(startId: String, start: StartsListItem?): StartScreenInput {
        if (start != null) return StartScreenInput.Item(start)
        val numericId = startId.toIntOrNull()
        return if (numericId != null) {
            StartScreenInput.Id(numericId)
        } else {
            StartScreenInput.Slug(startId)
        }
    }

    private fun child(
        config: RootStartsComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsComponent.Child =
        when (config) {
            is RootStartsComponent.Config.Start -> RootStartsComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    input = createStartScreenInput(config.startId, config.startItem),
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Starts ->
                RootStartsComponent.Child.Starts(
                    component = StartsScreenComponent(
                        componentContext = componentContext,
                        dataSource = scope.get(),
                        toDetail = {
                            navigation.pushNew(RootStartsComponent.Config.Start(it, it.name))
                        },
                        toSearch = {
                            navigation.pushNew(RootStartsComponent.Config.Search)
                        },
                        toSettings = {
                            navigation.pushNew(RootStartsComponent.Config.Settings)
                        }
                    ),
                )

            is RootStartsComponent.Config.Search -> RootStartsComponent.Child.Search(
                RootStartsSearchComponentBase(
                    context = componentContext,
                    pop = navigation::pop,
                )
            )

            is RootStartsComponent.Config.Settings -> RootStartsComponent.Child.Settings(
                RootSettingsComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )
        }
}
