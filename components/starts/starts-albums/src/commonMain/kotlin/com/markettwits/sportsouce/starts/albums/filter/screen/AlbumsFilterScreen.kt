package com.markettwits.sportsouce.starts.albums.filter.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.starts.albums.filter.component.AlbumsFilterComponent
import com.markettwits.sportsouce.starts.albums.filter.store.AlbumsFilterStore
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun AlbumsFilterScreen(component: AlbumsFilterComponent) {
    val state by component.state.collectAsState()
    DefaultModalBottomSheet(title = "Фильтры", edgeToEdgeContent = true,
        onDismissRequest = { component.obtainEvent(AlbumsFilterStore.Intent.Dismiss) }) {
        AlbumsFilterContent(state.year, component::obtainEvent)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
internal fun AlbumsFilterContent(year: Int?, event: (AlbumsFilterStore.Intent) -> Unit) {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val years = remember(currentYear) { listOf("Все годы") + (currentYear downTo 2018).map(Int::toString) }
    Column(Modifier.fillMaxWidth().sauceVerticalScroll(rememberScrollState())
        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        DropDownSpinner(selectedItem = year?.toString() ?: "Все годы", itemList = years,
            onItemSelected = { _, value -> event(AlbumsFilterStore.Intent.SelectYear(value.toIntOrNull())) }, textFiled = {
                OutlinedTextFieldBase(label = "Год старта", value = year?.toString() ?: "Все годы", readOnly = true, onValueChange = {})
            })
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SauceButton("Сбросить", { event(AlbumsFilterStore.Intent.Reset) }, Modifier.weight(1f), variant = SauceButtonVariant.Outlined)
            SauceButton("Применить", { event(AlbumsFilterStore.Intent.Apply) }, Modifier.weight(1f))
        }
    }
}
