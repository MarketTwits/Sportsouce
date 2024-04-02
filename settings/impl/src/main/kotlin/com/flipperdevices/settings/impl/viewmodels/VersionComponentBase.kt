package com.flipperdevices.settings.impl.viewmodels

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VersionComponentBase(
    componentContext: ComponentContext,
    private val selfUpdaterApi: SelfUpdaterApi,
    private val applicationParams: String
) : VersionComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val dialogFlow = MutableStateFlow(false)

    // override fun inProgress() = selfUpdaterApi.getInProgressState()

    override fun getDialogState() = dialogFlow.asStateFlow()

    override fun versionApp() = applicationParams

    override fun sourceInstall() = selfUpdaterApi.getInstallSourceName()

    override fun isSelfUpdateManualChecked() = selfUpdaterApi.isSelfUpdateCanManualCheck()

    override fun onCheckUpdates() {
        scope.launch(Dispatchers.Default) {
            val result = selfUpdaterApi.startCheckUpdate(manual = true)
            when (result) {
                SelfUpdateResult.NO_UPDATES -> dialogFlow.emit(true)
                else -> {}
            }
        }
    }

    override fun dismissDialog() {
        scope.launch(Dispatchers.Default) {
            dialogFlow.emit(false)
        }
    }
}
