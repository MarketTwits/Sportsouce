package com.markettwits.sportsouce.start.register.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.IntentAction
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.start.register.di.startRegistrationModule
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesComponentBase
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesOutput
import com.markettwits.sportsouce.start.register.presentation.registration.member.component.RegistrationMemberComponentBase
import com.markettwits.sportsouce.start.register.presentation.registration.member.component.RegistrationMemberInput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationInput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationOutput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationPageComponentBase
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore
import com.markettwits.sportsouce.start.register.presentation.success.RegisterSuccessComponentBase

/**
 * @param pop callback for navigate to back
 * @param content : first - startId, second - DistanceItem, third -
 *     paymentDisabled, fourth - paymentType
 */

class RootStartRegisterBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val input: StartDistancesInput,
) : ComponentContext by componentContext, RootStartRegister {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext(false)
    }
    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val intentAction: IntentAction = scope.get()

    private val stackNavigation = StackNavigation<RootStartRegister.Config>()
    private val initializedMemberContactPerson = mutableSetOf<MemberKey>()

    override val childStack: Value<ChildStack<*, RootStartRegister.Child>> = childStack(
        source = stackNavigation,
        serializer = RootStartRegister.Config.serializer(),
        initialConfiguration = getInitialConfig(input),
        handleBackButton = true,
        childFactory = ::childStack,
    )

    private fun childStack(
        config: RootStartRegister.Config,
        componentContext: ComponentContext,
    ): RootStartRegister.Child =
        when (config) {

            RootStartRegister.Config.StartRegistrationSuccess -> RootStartRegister.Child.StartRegistrationSuccess(
                RegisterSuccessComponentBase(
                    componentComponent = componentContext,
                    next = pop::invoke
                )
            )

            is RootStartRegister.Config.StartRegistrationPage -> RootStartRegister.Child.StartRegistrationPage(
                StartRegistrationPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    input = config.input,
                    output = StartRegistrationPageComponentOutputsImpl()
                )
            )

            is RootStartRegister.Config.RegistrationMember -> RootStartRegister.Child.RegistrationMember(
                RegistrationMemberComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    input = RegistrationMemberInput(
                        startStatement = config.startStatement,
                        membersProfile = config.startStatement.members,
                        memberId = config.memberId
                    ),
                    apply = { statement, _ ->
                        val registrationComponent = findRegistrationPageComponent()
                        val stage = registrationComponent
                            ?.state
                            ?.value
                            ?.stages
                            ?.filterIsInstance<StartRegistrationStagePage.Registration>()
                            ?.firstOrNull { it.id == config.stageId }
                        if (registrationComponent != null && stage != null) {
                            registrationComponent.obtainEvent(
                                StartRegistrationPageStore.Intent.UpdateStagePage(
                                    updateMemberInStage(stage, statement)
                                )
                            )
                            registrationComponent.obtainEvent(
                                StartRegistrationPageStore.Intent.OnPageSelected(config.stageId)
                            )
                        }
                        stackNavigation.pop()
                    },
                    pop = {
                        stackNavigation.pop()
                    }
                )
            )

            is RootStartRegister.Config.AuthFlow -> RootStartRegister.Child.AuthFlow(
                RootAuthFlowComponentBase(
                    context = componentContext,
                    goBack = pop::invoke,
                    goProfile = {
                        stackNavigation.replaceAll(
                            RootStartRegister.Config.StartRegistrationDistancesPage(
                                input
                            )
                        )
                    }
                )
            )

            is RootStartRegister.Config.StartRegistrationDistancesPage -> RootStartRegister.Child.StartRegistrationDistances(
                StartDistancesComponentBase(
                    componentContext = componentContext,
                    input = config.input,
                    output = StartDistancesOutputImpl(),
                    intentAction = intentAction
                )
            )
        }

    private inner class StartRegistrationPageComponentOutputsImpl : StartRegistrationOutput {
        override fun goBack() {
            stackNavigation.pop()
        }

        override fun goSuccess() {
            stackNavigation.replaceAll(RootStartRegister.Config.StartRegistrationSuccess)
        }

        override fun goAuth() {
            stackNavigation.replaceAll(RootStartRegister.Config.AuthFlow)
        }

        override fun openMember(stageId: Int, memberId: Int, startStatement: StartStatement) {
            val preparedStatement = prepareMemberStatementForFirstOpen(
                stageId = stageId,
                startStatement = startStatement
            )
            stackNavigation.bringToFront(
                RootStartRegister.Config.RegistrationMember(
                    stageId = stageId,
                    memberId = memberId,
                    startStatement = preparedStatement
                )
            )
        }
    }

    private inner class StartDistancesOutputImpl : StartDistancesOutput {
        override fun onClickGoBack() {
            pop()
        }

        override fun onClickDistance(distinctDistance: StartRegistrationInput) {
            stackNavigation.pushNew(
                RootStartRegister.Config.StartRegistrationPage(
                    distinctDistance
                )
            )
        }

    }

    private fun getInitialConfig(input: StartDistancesInput): RootStartRegister.Config {
        return RootStartRegister.Config.StartRegistrationDistancesPage(input)
    }

    private fun findRegistrationPageComponent(): com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationPageComponent? {
        val items = childStack.value.backStack + childStack.value.active
        return items
            .map { it.instance }
            .filterIsInstance<RootStartRegister.Child.StartRegistrationPage>()
            .lastOrNull()
            ?.component
    }

    private fun updateMemberInStage(
        stage: StartRegistrationStagePage.Registration,
        updatedStatement: StartStatement,
    ): StartRegistrationStagePage.Registration {
        val updatedStages = stage.distance.stages.map { stageWithStatement ->
            if (stageWithStatement.stage.id == updatedStatement.stageId) {
                stageWithStatement.copy(statement = updatedStatement)
            } else {
                stageWithStatement
            }
        }
        return stage.copy(distance = stage.distance.copy(stages = updatedStages))
    }

    private fun prepareMemberStatementForFirstOpen(
        stageId: Int,
        startStatement: StartStatement,
    ): StartStatement {
        val memberKey = MemberKey(stageId = stageId, memberStageId = startStatement.stageId)
        if (memberKey in initializedMemberContactPerson) return startStatement

        val registrationComponent = findRegistrationPageComponent() ?: return startStatement
        val registrationStage = registrationComponent.state.value.stages
            .filterIsInstance<StartRegistrationStagePage.Registration>()
            .firstOrNull { it.id == stageId }
            ?: return startStatement

        val hasAnotherContactPerson = registrationStage.distance.stages
            .map { it.statement }
            .any { statement ->
                statement.stageId != startStatement.stageId && statement.contactPerson
            }

        return if (hasAnotherContactPerson && startStatement.contactPerson) {
            initializedMemberContactPerson += memberKey
            startStatement.copy(contactPerson = false)
        } else {
            startStatement
        }
    }

    private data class MemberKey(
        val stageId: Int,
        val memberStageId: Int?,
    )
}
