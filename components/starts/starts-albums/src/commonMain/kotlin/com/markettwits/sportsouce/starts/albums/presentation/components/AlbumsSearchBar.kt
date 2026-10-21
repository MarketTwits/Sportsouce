package com.markettwits.sportsouce.starts.albums.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.components.topbar.sauceTopBarContainerColor
import com.markettwits.core_ui.items.theme.SauceTheme

/** Same toolbar layout and core field as StartsSearchBarInner. */
@Composable
internal fun AlbumsSearchBar(query: String, withFilter: Boolean, onQueryChanged: (String) -> Unit,
    onFilters: () -> Unit, onBack: () -> Unit) {
    val focus = LocalFocusManager.current
    Row(Modifier.fillMaxWidth().background(sauceTopBarContainerColor()).windowInsetsPadding(WindowInsets.statusBars).padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = SauceTheme.colors.textSecondary)
        }
        BoundlessTextFieldBase(modifier = Modifier.weight(1f).semantics { contentDescription = "Поиск фотоальбомов" }, value = query,
            onValueChange = onQueryChanged, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focus.clearFocus() }), placeholder = {
                Text("Поиск фотоальбомов", style = MaterialTheme.typography.bodyLarge, color = SauceTheme.colors.textMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            })
        if (query.isNotEmpty()) IconButton(onClick = { onQueryChanged("") }) {
            Icon(Icons.Default.Close, "Очистить поиск", tint = SauceTheme.colors.textSecondary)
        }
        IconButton(onClick = { focus.clearFocus(); onFilters() }) {
            Icon(Icons.AutoMirrored.Filled.Notes, "Фильтры", tint = if (withFilter) SauceTheme.colors.action else SauceTheme.colors.textSecondary)
        }
    }
}
