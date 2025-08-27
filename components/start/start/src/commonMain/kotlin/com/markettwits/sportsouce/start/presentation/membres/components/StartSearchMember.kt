package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreen
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartSearchMember(
    modifier: Modifier = Modifier,
    component: StartMembersScreen,
) {
    val query by component.filterValue.subscribeAsState()
    val st by component.state.collectAsState()

    val selectedTitles: List<String> = st.selectedFiltersUi.flatMap { grp ->
        if (grp.title == "Группа") emptyList() else grp.items.filterIsInstance<MembersFilterItem.Selected>()
            .map { it.title }
    }
    val selectedCount = selectedTitles.size

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    onClick = {
                        component.back()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
                BoundlessTextFieldBase(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(top = 5.dp),
                    value = query,
                    onValueChange = {
                        component.handleTextFiled(it)
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = "Поиск участника (Фамилия Имя)",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.bold(),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                    },
                )
                BadgedBox(badge = {
                    val text = if (selectedCount > 0) selectedCount.toString() else ""
                    Badge { Text(text) }
                }) {
                    IconButton(
                        onClick = { component.openFilter() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            contentDescription = "Notes",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
            if (selectedTitles.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxLines = 3,
                ) {
                    selectedTitles.forEach { title ->
                        FilterChipBase(
                            selected = true,
                            onClick = { component.removeSelectedFilter(title) },
                            label = title
                        )
                    }
                }
            }
        }
    }
}