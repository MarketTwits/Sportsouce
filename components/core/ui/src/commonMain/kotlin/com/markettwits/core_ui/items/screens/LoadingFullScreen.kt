package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase

@Composable
fun LoadingFullScreen(modifier: Modifier = Modifier, onClickBack: (() -> Unit)? = null) {
    Box(modifier.fillMaxSize()) {
        CircularProgressIndicatorBase(Modifier.align(Alignment.Center).semantics { contentDescription = "Загрузка" })
        if (onClickBack != null) BackFloatingActionButton(back = onClickBack)
    }
}
