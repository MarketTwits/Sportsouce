package com.markettwits.sportsouce.edit_profile.sign_out.presentation.components

import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.dialog.SauceDialog
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.component.EditProfileSignOutComponent
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.store.EditProfileSignOutStore

@Composable
fun EditProfileSignOutScreenDialog(component: EditProfileSignOutComponent) {
    SauceDialog(onDismissRequest = {
        component.obtainEvent(EditProfileSignOutStore.Intent.Dismiss)
    }
    ) {
        EditProfileSignOutContent(
            dismiss = {
                component.obtainEvent(EditProfileSignOutStore.Intent.Dismiss)
            }, apply = {
                component.obtainEvent(EditProfileSignOutStore.Intent.SignOut)
            })
    }
}