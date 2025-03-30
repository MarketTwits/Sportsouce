package com.markettwits.sportsouce.edit_profile.edit_menu.presentation.component

import com.arkivanov.decompose.ComponentContext

class EditProfileMenuComponentComponentBase(
    componentContext: ComponentContext,
    private val output: (EditProfileMenuComponentComponent.OutPut) -> Unit,
) : EditProfileMenuComponentComponent, ComponentContext by componentContext {

    override fun obtainOutPut(output: EditProfileMenuComponentComponent.OutPut) {
        output(output)
    }
}
