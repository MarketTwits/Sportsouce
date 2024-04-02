package com.markettwits.selfupdater.components.selft_update.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
internal fun SelfUpdateContent(
    currentVersion: String,
    changes: String,
    isLoading: Boolean,
    isSuccess: Boolean,
    isFailed: Boolean,
    message: String,
    consumed: () -> Unit,
    onClickStartUpdate: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelfUpdateAvailable()
        Spacer(modifier = Modifier.padding(14.dp))
        SelfUpdateWhatsNew(actualVersion = currentVersion, changes = changes)
        Spacer(modifier = Modifier.padding(14.dp))
        SelfUpdateButton(isLoading = isLoading, onClickStartUpdate = onClickStartUpdate)
    }
    if (isSuccess || isFailed) {
        SelfUpdateStartedDialog(message = message, result = isSuccess) {
            consumed()
        }
    }
}