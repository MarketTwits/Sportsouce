package com.flipperdevices.settings.impl.viewmodels

import com.flipperdevices.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface VersionComponent {
    //fun inProgress() : StateFlow<Boolean>

    fun getDialogState(): StateFlow<Boolean>

    fun versionApp(): String
    fun sourceInstall(): String

    fun isSelfUpdateManualChecked(): Boolean

    fun onCheckUpdates()

    fun dismissDialog()
}