package com.markettwits.edit_profile.edit_menu.presentation.component

import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileMenuComponentComponent {
    val state: StateFlow<EditProfileMenuStore.State>
    fun obtainEvent(intent: EditProfileMenuStore.Intent)
    fun obtainOutPut(output: OutPut)
    sealed interface OutPut {
        data object GoBack : OutPut
        data object GoChangePassword : OutPut
        data object GoSocialNetwork : OutPut
        data object GoChangeProfileInfo : OutPut
        data object GoProfileAbout : OutPut
        data object GoProfileImage : OutPut
        data object GoSignOut : OutPut
    }
}

interface EditProfileMenuOutputProvide {
    fun obtainOutPut(output: EditProfileMenuComponentComponent.OutPut)
}
