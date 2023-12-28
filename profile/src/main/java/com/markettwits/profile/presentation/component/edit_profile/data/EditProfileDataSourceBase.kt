package com.markettwits.profile.presentation.component.edit_profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import ru.alexpanov.core_network.api.SportsouceApi

class EditProfileDataSourceBase(
    private val mapper: RemoteUserToEditProfileMapper,
    private val authDataSource: AuthDataSource,
    private val cloud : SportsouceApi
) : EditProfileDataSource{
    override suspend fun changeProfileInfo(current: List<EditProfileUiPage>, base: User) {
        try {
            val request = mapper.map(current, base)
            val token = authDataSource.updateToken()
            cloud.changeProfileInfo(request, token)
        }catch (e : Exception){

        }
    }
}