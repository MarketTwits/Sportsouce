package com.markettwits.profile.presentation.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.auth.R
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(component: SignInScreen) {

    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    val scope = rememberCoroutineScope()
    val state by component.state.subscribeAsState()
    val fieldState by component.fieldState.subscribeAsState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.im_welcom_image),
                contentDescription = "welcom image"
            )
            Text(
                text = "Добро пожаловать на SportSouce !",
                fontFamily = FontNunito.bold,
                fontSize = 24.sp,
                color = SportSouceColor.SportSouceBlue
            )
            val modifier = Modifier.padding(10.dp)
            AuthTextField(
                modifier = modifier,
                label = "Почта",
                value = fieldState.email,
                isError = state is SignInUiState.Error,
            ) {
                component.handleEmail(it)
            }
            AuthTextField(
                modifier = modifier,
                label = "Пароль",
                value = fieldState.password,
                visualTransformation = PasswordVisualTransformation(),
                isError = state is SignInUiState.Error
            ) {
                component.handlePassword(it)
            }
            ForgotPasswordText(modifier = modifier)
            AuthButton(
                modifier = modifier,
                "Войти",
                enabled = state != SignInUiState.Loading && fieldState.enabled,
                loading = state is SignInUiState.Loading
            ) {
                component.logIn()
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Snackbar(
                contentColor = Color.White,
                containerColor = SportSouceColor.SportSouceLightRed,
                snackbarData = it
            )
        }
        if ((state is SignInUiState.Error)) {
            if (!(state as SignInUiState.Error).messageShow) {
                LaunchedEffect(snackbarHostState) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = (state as SignInUiState.Error).message,
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                        component.messageHasBeenShowed()
                    }
                }
            }
        }
    }
}
@Composable
fun ForgotPasswordText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Забыли пароль ?",
            fontFamily = FontNunito.regular,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = SportSouceColor.SportSouceBlue,
            disabledContainerColor = SportSouceColor.SportSouceBlue.copy(alpha = 0.3f),
        ),
        enabled = enabled,
        shape = Shapes.medium,
        onClick = { onClick() }
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(
                text = title,
                fontFamily = FontNunito.bold,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp)
        },
        visualTransformation = visualTransformation,
        isError = isError,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = SportSouceColor.SportSouceBlue,
            focusedIndicatorColor = SportSouceColor.SportSouceBlue,
            unfocusedIndicatorColor = SportSouceColor.SportSouceBlue,
            errorIndicatorColor = SportSouceColor.SportSouceLightRed,
            errorContainerColor = Color.White,
            errorTextColor = SportSouceColor.SportSouceLightRed,
            focusedLabelColor = SportSouceColor.SportSouceBlue,
            unfocusedTextColor = SportSouceColor.SportSouceBlue
        ),
        textStyle = TextStyle(fontFamily = FontNunito.medium),
        maxLines = 1,
        onValueChange = onValueChange,
    )
}

@Composable
private fun AuthSnackBar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    message: String,
    close: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    ExtendedFloatingActionButton(
        modifier = modifier.fillMaxWidth(),
        text = { Text("Show snackbar") },
        icon = {
            Icon(
                modifier = Modifier.clickable {
                    close()
                },
                imageVector = Icons.Filled.Image,
                contentDescription = ""
            )
        },
        onClick = {

            val job = scope.launch {
                val result = snackbarHostState
                    .showSnackbar(
                        message = message,
                        actionLabel = "Action",
                        // Defaults to SnackbarDuration.Short
                        duration = SnackbarDuration.Short
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        close()
                    }

                    SnackbarResult.Dismissed -> {
                        close()
                    }
                }
                delay(500)
            }
            job.cancel()
        }
    )
}

@Preview()
@Composable
private fun AuthScreenPreview() {
    AuthScreen(MockSignInScreenComponent())
}

