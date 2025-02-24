package com.markettwits.members.member_detail.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore
import com.markettwits.members.member_detail.presentation.store.MemberDetailStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberDetailComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: MemberDetailStoreFactory,
    private val member: ProfileMember,
    private val dismiss: () -> Unit,
    private val memberDeleted: () -> Unit,
    private val onClickEdit: (ProfileMember) -> Unit
) : MemberDetailComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(member)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<MemberDetailStore.State> = store.stateFlow

    override fun obtainEvent(intent: MemberDetailStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is MemberDetailStore.Label.Dismiss -> dismiss()
                    is MemberDetailStore.Label.OnClickEdit -> onClickEdit(member)
                    is MemberDetailStore.Label.MemberDeleted -> memberDeleted()
                }
            }
        }
    }
}
