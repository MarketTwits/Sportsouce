package com.markettwits.bottom_bar.component.storage

import com.markettwits.bottom_bar.components.defaultMenuItems
import com.markettwits.bottom_bar.model.BottomBarState
import com.markettwits.bottom_bar.model.Configuration
import com.markettwits.settings.api.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class BottomBarStorageImpl(private val repository: SettingsRepository) : BottomBarStorage {

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        repository.observeSettings().onEach { settings ->
            state.update {
                it.copy(showLabel = !settings.bottomPanel.isCompact)
            }
        }.launchIn(scope)
    }

    override val state: MutableStateFlow<BottomBarState> = MutableStateFlow(
        BottomBarState(
            showTopBar = true,
            showLabel = true,
            selectedTab = Configuration.Home,
            items = defaultMenuItems()
        )
    )

    override fun onSelectTab(configuration: Configuration) {
        state.update {
            it.copy(selectedTab = configuration)
        }
    }

    override fun show() {
        state.update {
            it.copy(showTopBar = true)
        }
    }

    override fun hide() {
        state.update {
            it.copy(showTopBar = false)
        }
    }
}