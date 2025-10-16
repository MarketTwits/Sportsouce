package com.markettwits.sportsouce.club.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.bottom_bar.di.bottomBarModule
import com.markettwits.sportsouce.club.dashboard.di.clubDashboardModule
import com.markettwits.sportsouce.club.dashboard.di.createDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.info.di.clubInfoModule
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponentBase
import com.markettwits.sportsouce.club.registration.di.createClubRegistrationComponent
import com.markettwits.sportsouce.club.registration.di.workoutRegistrationModule
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponent
import com.markettwits.sportsouce.club.root.RootClubComponent.SlotConfig.WorkoutRegistration
import com.markettwits.sportsouce.club.schedule.component.ScheduleComponent
import com.markettwits.sportsouce.club.schedule.component.ScheduleComponentBase
import com.markettwits.sportsouce.club.subscription.component.SubscriptionPricingComponent
import com.markettwits.sportsouce.club.subscription.component.SubscriptionPricingComponentBase

class RootClubComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
) : ComponentContext by componentContext, RootClubComponent {

    private var dashboardComponent: ClubDashboardComponent? = null

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(
            clubDashboardModule,
            clubInfoModule,
            bottomBarModule,
            workoutRegistrationModule
        )
    )

    private val stackNavigation = StackNavigation<RootClubComponent.StackConfig>()

    private val slotNavigation = SlotNavigation<RootClubComponent.SlotConfig>()

    override val childSlot: Value<ChildSlot<*, RootClubComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            serializer = RootClubComponent.SlotConfig.serializer(),
            handleBackButton = true,
            childFactory = ::createSlotChild
        )

    override val stackChildStack: Value<ChildStack<*, RootClubComponent.StackChild>> =
        childStack(
            source = stackNavigation,
            serializer = RootClubComponent.StackConfig.serializer(),
            initialConfiguration = RootClubComponent.StackConfig.Dashboard,
            handleBackButton = true,
            childFactory = ::childStack,
        )

    private fun createSlotChild(
        config: RootClubComponent.SlotConfig,
        componentContext: ComponentContext,
    ): RootClubComponent.SlotChild {
        return when (config) {
            is WorkoutRegistration -> RootClubComponent.SlotChild.WorkoutRegistration(
                createClubRegistrationComponent(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    type = config.type
                ) {
                    workoutRegistrationOuPuts(it)
                }
            )

            is RootClubComponent.SlotConfig.ClubInfoDetail -> RootClubComponent.SlotChild.ClubInfoDetail(
                ClubInfoComponentBase(
                    componentContext = componentContext,
                    dismiss = { slotNavigation.dismiss() },
                    onOpenRegistration = { type ->
                        slotNavigation.dismiss {
                            slotNavigation.activate(WorkoutRegistration(type))
                        }
                    },
                    onOpenSchedule = {
                        slotNavigation.dismiss {
                            stackNavigation.pushNew(RootClubComponent.StackConfig.Schedule)
                        }
                    },
                    selectedTab = config.selectedTab,
                    bottomSheetData = config.bottomSheetData,
                    storeFactory = scope.get()
                )
            )
        }
    }

    private fun childStack(
        stackConfig: RootClubComponent.StackConfig,
        componentContext: ComponentContext,
    ): RootClubComponent.StackChild =
        when (stackConfig) {
            RootClubComponent.StackConfig.Dashboard -> {
                val component = scope.createDashboardComponent(
                    componentContext = componentContext,
                    output = {
                        dashboardOuPuts(it)
                    }
                )
                dashboardComponent = component
                RootClubComponent.StackChild.Dashboard(component)
            }

            is RootClubComponent.StackConfig.SubscriptionPricing -> {
                val storeFactory = scope.get<StoreFactory>()
                val clubRepository = scope.get<com.markettwits.sportsouce.club.common.domain.ClubRepository>()
                val component = SubscriptionPricingComponentBase(
                    componentContext = componentContext,
                    storeFactory = storeFactory,
                    clubRepository = clubRepository,
                    output = { subscriptionPricingOutputs(it) },
                )
                RootClubComponent.StackChild.SubscriptionPricing(component)
            }

            is RootClubComponent.StackConfig.Schedule -> {
                val storeFactory = scope.get<StoreFactory>()
                val component = ScheduleComponentBase(
                    componentContext = componentContext,
                    storeFactory = storeFactory,
                    output = { scheduleOutputs(it) },
                    repository = scope.get()
                )
                RootClubComponent.StackChild.Schedule(component)
            }
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun dashboardOuPuts(output: ClubDashboardComponent.Output) {
        when (output) {
            is ClubDashboardComponent.Output.Dismiss -> pop()
            is ClubDashboardComponent.Output.Subscription -> slotNavigation.activate(
                WorkoutRegistration(output.type)
            )

            is ClubDashboardComponent.Output.GoSubscriptions -> {
                stackNavigation.push(
                    RootClubComponent.StackConfig.SubscriptionPricing("default")
                )
            }

            is ClubDashboardComponent.Output.GoSchedule -> {
                stackNavigation.push(
                    RootClubComponent.StackConfig.Schedule
                )
            }

            is ClubDashboardComponent.Output.OpenClubInfoDetail -> {
                slotNavigation.activate(
                    RootClubComponent.SlotConfig.ClubInfoDetail(
                        selectedTab = output.selectedTab,
                        bottomSheetData = output.bottomSheetData
                    )
                )
            }
        }
    }

    private fun workoutRegistrationOuPuts(output: WorkoutRegistrationComponent.Output) {
        when (output) {
            is WorkoutRegistrationComponent.Output.Dismiss -> {
                slotNavigation.dismiss()
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun subscriptionPricingOutputs(output: SubscriptionPricingComponent.Output) {
        when (output) {
            is SubscriptionPricingComponent.Output.Dismiss -> {
                stackNavigation.pop()
            }

            is SubscriptionPricingComponent.Output.Registration -> {
                slotNavigation.activate(
                    WorkoutRegistration(output.type)
                )
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun scheduleOutputs(output: ScheduleComponent.Output) {
        when (output) {
            is ScheduleComponent.Output.Dismiss -> {
                stackNavigation.pop()
            }

            is ScheduleComponent.Output.Registration -> {
                slotNavigation.activate(
                    WorkoutRegistration(output.type)
                )
            }
        }
    }

}