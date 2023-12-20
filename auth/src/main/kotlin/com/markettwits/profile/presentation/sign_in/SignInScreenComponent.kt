package com.markettwits.profile.presentation.sign_in

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.data.AuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SignInScreenComponent(
    private val service : AuthDataSource
) : SignInScreen {
    private val _state = MutableValue("")
    override val state: Value<String> = _state
    val scope = CoroutineScope(Dispatchers.Main)

    override fun logIn(password: String, email: String) {
        scope.launch {
            service.auth(email, password)
        }
    }

    override fun show(){
        scope.launch {
            _state.value =  service.show()
        }
    }
}