package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore.Intent
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore.State

@Composable
internal fun StartFilterContent(state: State, onEvent: (Intent) -> Unit) {
    val filter = state.filter
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.widthIn(max = 640.dp).fillMaxWidth()) {
            when {
                state.isLoading -> LoadingFullScreen()
                state.loadFailed -> Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Не удалось загрузить фильтры", style = MaterialTheme.typography.bodyMedium,
                        color = SauceTheme.colors.textSecondary)
                    SauceButton("Повторить", { onEvent(Intent.Launch) }, variant = SauceButtonVariant.Outlined)
                }
                filter.items.isNotEmpty() -> {
                    Column(
                        Modifier.weight(1f, fill = false)
                            .sauceVerticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .padding(top = 8.dp, bottom = 20.dp),
                    ) {
                        StartFilterList(filter, onEvent)
                    }
                    HorizontalDivider(color = SauceTheme.colors.border)
                    FilterButtonSelectionPanel(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                        onClickReset = { onEvent(Intent.Reset) },
                        onClickApply = { onEvent(Intent.Apply) },
                        resetEnabled = !filter.filterIsEmpty(),
                        applyEnabled = !filter.hasInvalidPeriod,
                    )
                }
            }
        }
    }
}
