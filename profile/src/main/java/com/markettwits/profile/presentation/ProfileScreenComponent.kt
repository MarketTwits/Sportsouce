package com.markettwits.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple

class ProfileScreenComponent(
    context: ComponentContext,
    private val profileInstanceKeeper: ProfileInstanceKeeper,
    private val launchPolicy: ProfileLaunchPolicy
) : ProfileScreen, ComponentContext by context {
    private val keeper = instanceKeeper.getOrCreateSimple { profileInstanceKeeper }
    override val nameState: Value<ProfileUiState> = keeper.state

    init {
        keeper.init()
    }

    override fun goToSignInScreen() {
        keeper.openAuthScreen()
    }

    override fun exit() {
        keeper.exit()
    }

    override fun init() {
        if (launchPolicy is ProfileLaunchPolicy.Update) {
            keeper.init()
        }
    }
}