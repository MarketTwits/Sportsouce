package com.markettwits.registrations.registrations_list.presentation.components.filter

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationsFilterList(
    modifier: Modifier = Modifier, items: List<FilterItem>,
    onClick: (FilterItem) -> Unit
) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        items.forEach {
            RegistrationsFilterItem(
                modifier = Modifier.padding(10.dp),
                value = it.value,
                checked = it.checked,
                onClick = {
                    onClick(it)
                })
        }
    }
}