package com.markettwits.start.presentation.member.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.presentation.member.store.RegistrationMemberStore
import com.markettwits.start.presentation.registration.components.StartRegistrationTopBar

@Composable
fun MemberScreen(component: RegistrationMemberComponent) {
    val state by component.model.collectAsState()
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            StartRegistrationTopBar(goBack = {
                component.obtainEvent(RegistrationMemberStore.Intent.Pop)
            })
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = SportSouceColor.SportSouceLightRed,
                    snackbarData = it
                )
            }
        }
    ) {
        MemberScreenContent(
            modifier = Modifier.padding(it),
            userNumber = state.userNumber,
            statement = state.value,
            onValueChanged = {
                component.obtainEvent(RegistrationMemberStore.Intent.ChangeFiled(it))
            },
            onClickContinue = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnClickContinue)
            }
        )
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}

@Preview
@Composable
private fun MemberScreenPreview() {
    // MemberScreen()
}

private val fakeStartStatement = StartStatement(
    name = "John",
    surname = "Doe",
    birthday = "1990-01-01",
    age = 32,
    email = "john.doe@example.com",
    sex = "Male",
    city = "CityA",
    team = "TeamX",
    phone = "+1234567890",
    promocode = "ABC123",
    price = "$100",
    contactPerson = true,
    cities = listOf(
        StartStatement.City(id = 1, name = "CityA"),
        StartStatement.City(id = 2, name = "CityB"),
        StartStatement.City(id = 3, name = "CityC")
    ),
    teams = listOf(
        StartStatement.Team(id = 1, name = "TeamX"),
        StartStatement.Team(id = 2, name = "TeamY"),
        StartStatement.Team(id = 3, name = "TeamZ")
    ),
    sexList = listOf(
        StartStatement.Sex(id = 1, name = "Male"),
        StartStatement.Sex(id = 2, name = "Female")
    ),
    paymentDisabled = false
)