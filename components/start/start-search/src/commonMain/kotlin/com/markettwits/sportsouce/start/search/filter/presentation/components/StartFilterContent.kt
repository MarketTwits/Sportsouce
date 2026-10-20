package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi

@Composable
fun StartFilterContent(
    modifier: Modifier = Modifier,
    filterUi: StartFilterUi,
    isLoading: Boolean,
    onClickReset: () -> Unit,
    onClickApply: () -> Unit,
    onItemSelected: (String, Int, Boolean) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        if (isLoading) LoadingFullScreen()
        if (filterUi.items.isNotEmpty()) {
            StartFilterList(startFilter = filterUi, onEvent = onItemSelected)
            FilterButtonSelectionPanel(onClickReset = onClickReset, onClickApply = onClickApply)
        }
    }
}
