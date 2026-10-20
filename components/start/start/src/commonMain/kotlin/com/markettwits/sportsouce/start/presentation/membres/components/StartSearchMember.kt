package com.markettwits.sportsouce.start.presentation.membres.components

import com.markettwits.core_ui.items.components.topbar.sauceTopBarContainerColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.common.components.StartListSearchBar
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreen
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartSearchMember(modifier: Modifier = Modifier, component: StartMembersScreen) {
    val query by component.filterValue.subscribeAsState()
    val state by component.state.collectAsState()
    val selectedTitles = state.selectedFiltersUi.flatMap { group ->
        if (group.title == "Группа") emptyList() else group.items.filterIsInstance<MembersFilterItem.Selected>().map { it.title }
    }
    Surface(modifier.fillMaxWidth(), color = sauceTopBarContainerColor()) {
        Column(Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp)) {
            StartListSearchBar(query, component::handleTextFiled, component::back,
                onClear = { component.handleTextFiled("") }, filterCount = selectedTitles.size, onFilter = component::openFilter)
            if (selectedTitles.isNotEmpty()) Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                selectedTitles.forEach { title -> FilterChipBase(modifier = Modifier.widthIn(max = 260.dp), selected = true, onClick = { component.removeSelectedFilter(title) }, label = title) }
            }
        }
    }
}
