package com.markettwits.start.register.presentation.success

import com.arkivanov.decompose.ComponentContext

class RegisterSuccessComponentBase(
    componentComponent: ComponentContext,
    private val next: () -> Unit
) : RegisterSuccessComponent, ComponentContext by componentComponent {
    override fun onClickContinue() {
        next()
    }
}