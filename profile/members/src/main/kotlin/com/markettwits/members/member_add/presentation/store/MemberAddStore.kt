package com.markettwits.members.member_add.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Intent
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Label
import com.markettwits.members.member_add.presentation.store.MemberAddStore.State

interface MemberAddStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

}
