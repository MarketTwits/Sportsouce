package com.markettwits.sportsouce.edit_profile.info.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.edit_profile.info.presentation.component.EditProfileInfoComponent
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore

@Composable
fun EditProfileInfoScreen(component: EditProfileInfoComponent) {
    val state by component.state.collectAsState()

    EditProfileInfoFieldsContent(
        state = state,
        onUserChange = { newValue ->
            component.obtainEvent(EditProfileInfoStore.Intent.UpdateState(newValue))
        },
        onClickSave = {
            component.obtainEvent(EditProfileInfoStore.Intent.OnClickUpdate)
        },
        onClickRetry = {
            component.obtainEvent(EditProfileInfoStore.Intent.Retry)
        },
        onClickGoBack = {
            component.obtainEvent(EditProfileInfoStore.Intent.GoBack)
        },
        onConsume = {
            component.obtainEvent(EditProfileInfoStore.Intent.OnConsumedEvent)
        }
    )
}
