package com.markettwits.start.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.registration.components.StartRegistrationTopBar
import com.markettwits.start.presentation.registration.components.content.StartRegistrationSuccessContent
import com.markettwits.start.presentation.registration.store.StartRegistrationStore

@Composable
fun StartRegistrationScreen(component: StartRegistrationComponent) {
    val state by component.value.collectAsState()

        Column{
            StartRegistrationTopBar(goBack = {
                component.obtainEvent(StartRegistrationStore.Intent.GoBack)
            })
            if (state.isLoading) {
                LoadingScreen()
            }
            if (state.isError) {
                FailedScreen(
                    message = state.message,
                    onClickHelp = { /*TODO*/ }
                ) {

                }
            }
            val statement = state.startStatement
            if (statement != null) {
                StartRegistrationSuccessContent(statement = statement, onValueChanged = {
                    component.obtainEvent(StartRegistrationStore.Intent.ChangeFiled(it))
                })

            }
        }
}