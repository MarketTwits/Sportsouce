package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.component.EditProfileSignOutComponent
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore

@Composable
fun EditProfileSignOutScreenDialog(component: EditProfileSignOutComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileSignOutStore.Intent.Dismiss)
    }
    ) {
        EditProfileSignOutContent(
            modifier = Modifier.padding(10.dp),
            dismiss = {
                component.obtainEvent(EditProfileSignOutStore.Intent.Dismiss)
            }, apply = {
                component.obtainEvent(EditProfileSignOutStore.Intent.SignOut)
            })
    }
}