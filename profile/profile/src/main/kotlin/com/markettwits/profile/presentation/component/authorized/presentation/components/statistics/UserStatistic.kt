package com.markettwits.profile.presentation.component.authorized.presentation.components.statistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
internal fun UserStartStatistic(modifier: Modifier = Modifier, items: List<BarchartInput>) {
    var moduleDialog by rememberSaveable {
        mutableStateOf(false)
    }
    UserStartStatisticContent(
        modifier = modifier,
        items = items
    ) {
        moduleDialog = !moduleDialog
    }
    if (moduleDialog) {
        Dialog(onDismissRequest = { moduleDialog = false }) {
            UserStatisticInfo(onClickOk = {
                moduleDialog = false
            })
        }
    }
}