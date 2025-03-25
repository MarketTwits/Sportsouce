package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.FontNunito
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
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primary)
            .padding(10.dp)
    ) {
        if (isLoading) {
            LoadingFullScreen()
        }
        if (filterUi.items.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Фильтр",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp
                )
                Column {
                    StartFilterList(startFilter = filterUi) { item, index, single ->
                        onItemSelected(item, index, single)
                    }
                }
                FilterButtonSelectionPanel(
                    onClickReset = onClickReset, onClickApply = onClickApply
                )
            }
        }
    }
}