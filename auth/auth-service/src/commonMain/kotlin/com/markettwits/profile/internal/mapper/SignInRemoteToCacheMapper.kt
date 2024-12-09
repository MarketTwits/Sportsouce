package com.markettwits.profile.internal.mapper

import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.profile.internal.database.data.entities.CredentialRealmCache

internal interface SignInRemoteToCacheMapper {
    fun map(remote: SignInResponseSuccess, password: String): CredentialRealmCache
}