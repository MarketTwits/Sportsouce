package com.markettwits.sportsouce.profile.registrations.presentation.list.components.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistrationsToolbar(
    modifier: Modifier = Modifier,
    component: RegistrationsComponent,
) {
    val state by component.value.collectAsState()

    val selectedFilters = state.filter.filter { it.checked }
    val selectedCount = selectedFilters.size

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = {
                        component.obtainEvent(RegistrationsStore.Intent.Pop)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "ArrowBack",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp),
                    text = "Мои регистрации",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                BadgedBox(
                    badge = {
                        val text = if (selectedCount > 0) selectedCount.toString() else ""
                        Badge { Text(text) }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    IconButton(
                        onClick = { component.openFilter() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

            if (selectedFilters.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxLines = 3,
                ) {
                    selectedFilters.forEach { filterItem ->
                        FilterChipBase(
                            selected = true,
                            onClick = { component.toggleFilter(filterItem) },
                            label = filterItem.value
                        )
                    }
                }
            }
        }
    }
}