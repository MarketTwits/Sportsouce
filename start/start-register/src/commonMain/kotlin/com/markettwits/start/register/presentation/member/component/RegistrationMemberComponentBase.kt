package com.markettwits.start.register.presentation.member.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationMemberComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: RegistrationMemberStoreFactory,
    private val startStatement: StartStatement,
    private val membersProfile: List<ProfileMember>,
    private val memberId: Int,
    private val pop: () -> Unit,
    private val apply: (StartStatement, Int) -> Unit
) :
    RegistrationMemberComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        storeFactory.create(memberId, startStatement, membersProfile)
    }
    override val model: StateFlow<RegistrationMemberStore.State> = store.stateFlow
    override fun obtainEvent(event: RegistrationMemberStore.Intent) {
        store.accept(event)
    }

    private val backCallback = BackCallback { store.accept(RegistrationMemberStore.Intent.Pop) }

    init {
        backHandler.register(backCallback)
        scope.launch {
            store.labels.collect {
                when (it) {
                    is RegistrationMemberStore.Label.OnClickContinue -> apply(
                        it.startStatement,
                        memberId
                    )

                    is RegistrationMemberStore.Label.OnClickPop -> pop()
                }
            }
        }
    }
}
