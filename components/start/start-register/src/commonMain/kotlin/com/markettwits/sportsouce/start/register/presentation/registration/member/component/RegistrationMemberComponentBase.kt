package com.markettwits.sportsouce.start.register.presentation.registration.member.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationMemberComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: RegistrationMemberStoreFactory,
    private val input: RegistrationMemberInput,
    private val pop: () -> Unit,
    private val apply: (StartStatement, Int) -> Unit,
) : RegistrationMemberComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            userNumber = input.memberId,
            startStatement = input.startStatement,
            startMembers = input.membersProfile
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RegistrationMemberStore.State> = store.stateFlow

    override fun obtainEvent(event: RegistrationMemberStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is RegistrationMemberStore.Label.OnClickContinue -> apply(
                        it.startStatement,
                        input.memberId
                    )

                    is RegistrationMemberStore.Label.OnClickPop -> pop()
                }
            }
        }
    }
}
