package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStoreFactory
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.emptyProfileMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberEditComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: MemberEditStoreFactory,
    private val profileMember: ProfileMember?,
    private val pop: () -> Unit,
    private val apply: (ProfileMember) -> Unit,
) : MemberEditComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        if (profileMember != null) {
            storeFactory.create(profileMember, MemberEditComponent.Mode.Edit)
        } else {
            storeFactory.create(emptyProfileMember, MemberEditComponent.Mode.Add)
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<MemberEditStore.State> = store.stateFlow

    override fun obtainEvent(intent: MemberEditStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    MemberEditStore.Label.Dismiss -> pop()
                    is MemberEditStore.Label.UpdateSuccess -> apply(it.member)
                }
            }
        }
    }
}
