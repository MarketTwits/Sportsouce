package com.markettwits.profile.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.profile.presentation.sign_in.SignInUiState
import java.lang.Exception

interface SignInRemoteToUiMapper {
    fun map(remote: SignInResponseSuccess): SignInUiState
    fun map(exception: Exception): SignInUiState
    fun map(message : String) : SignInUiState

    class Base : SignInRemoteToUiMapper {
        override fun map(remote: SignInResponseSuccess): SignInUiState = SignInUiState.Success
        override fun map(exception: Exception): SignInUiState =
            SignInUiState.Error(exception.message.toString())

        override fun map(message: String): SignInUiState =
            SignInUiState.Error(message)

    }
}