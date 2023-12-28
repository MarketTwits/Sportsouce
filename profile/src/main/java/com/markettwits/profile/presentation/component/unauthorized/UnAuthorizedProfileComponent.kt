package com.markettwits.profile.presentation.component.unauthorized

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class UnAuthorizedProfileComponent(
    context: ComponentContext,
    private val service: ProfileDataSource,
    private val goAuthProfile: () -> Unit,
    private val goSignIn: () -> Unit
) : UnAuthorizedProfile, ComponentContext by context {
    val scope = CoroutineScope(Dispatchers.Main)
    init {
        check()
    }
    fun check() {
        scope.launch {
            val state = service.checkAuth()
            if (state) {
                goAuthProfile()
            }
        }
    }

    override fun signIn() {
        goSignIn()
    }
}