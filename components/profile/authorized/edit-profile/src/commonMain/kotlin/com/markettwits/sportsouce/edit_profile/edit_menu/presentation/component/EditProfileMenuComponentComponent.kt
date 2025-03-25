package com.markettwits.sportsouce.edit_profile.edit_menu.presentation.component

interface EditProfileMenuComponentComponent {
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
