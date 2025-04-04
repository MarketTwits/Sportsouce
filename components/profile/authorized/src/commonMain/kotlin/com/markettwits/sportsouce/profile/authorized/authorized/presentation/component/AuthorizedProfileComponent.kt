package com.markettwits.sportsouce.profile.authorized.authorized.presentation.component

import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import kotlinx.coroutines.flow.StateFlow

interface AuthorizedProfileComponent {

    val state: StateFlow<AuthorizedProfileStore.State>

    fun obtainEvent(intent: AuthorizedProfileStore.Intent)

    fun obtainOutput(outPut: Output)

    sealed interface Output {
        data object AllRegistries : Output
        data object EditProfile : Output
        data object Club : Output
        data object MyRegistries : Output
        data class StartOrder(val startOrderInfo: StartOrderInfo) : Output
        data object SocialNetwork : Output
        data object Members : Output
        data class Start(val startId: Int) : Output
        data object Settings : Output
        data object UserOrders : Output
    }
}