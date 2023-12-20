package com.markettwits.profile.presentation.sign_in

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun AuthScreen(component : SignInScreen) {
    var login by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }
    val state = component.state.subscribeAsState()
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        Column {
            TextField(value = login, onValueChange = {
                login = it
            })
            TextField(value = password, onValueChange = {
                password = it
            })
            Button(onClick = {
                component.logIn(password, login)
            }) {
                Text(text = "Log in ")
            }
            Button(onClick = {
                component.show()
            }) {
                Text(text = "Show !")
            }
            Text(text = state.value, color = Color.Black)
        }
    }
}

