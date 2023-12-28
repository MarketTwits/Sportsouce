package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.component.edit_profile.data.EditProfileDataSource
import com.markettwits.profile.presentation.component.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer


class EditProfileComponent(
    context: ComponentContext,
    private val currentUser: User,
    private val mapper: RemoteUserToEditProfileMapper,
    private val service : EditProfileDataSource,
    private val goBack: () -> Unit,
) : EditProfile, ComponentContext by context {
    override val user: MutableValue<User> = MutableValue(currentUser)

    override val page: MutableValue<List<EditProfileUiPage>> = MutableValue(
        stateKeeper.consume(PAGE_STATE_KEY, ListSerializer(EditProfileUiPage.serializer()))
            ?: mapper.map(currentUser)
    )
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        stateKeeper.register(
            key = PAGE_STATE_KEY,
            ListSerializer(EditProfileUiPage.serializer())
        ) { page.value }
    }

    override fun pop() = goBack()
    override fun saveChanges() {
        scope.launch {
            service.changeProfileInfo(page.value, currentUser)
        }
    }

    override fun obtainTextFiled(value: EditProfileUiPage) {
        updatePage(value)
    }

    private fun <T : EditProfileUiPage> updatePage(updatedUser: T) {
        val updatedList = page.value.map { page ->
            when {
                page::class == updatedUser::class -> updatedUser
                else -> page
            }
        }
        page.value = updatedList
    }

    private companion object {
        const val PAGE_STATE_KEY = "PAGE_STATE"
    }
}