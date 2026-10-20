package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import com.markettwits.core_ui.items.components.topbar.sauceTopBarContainerColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme


@Composable
fun StartsSearchBarInner(
    modifier: Modifier = Modifier,
    query: String = "",
    isWithFilter: Boolean,
    onQueryChanged: (String) -> Unit,
    onBrushClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onClickBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(sauceTopBarContainerColor())
            .windowInsetsPadding(WindowInsets.statusBars),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = { onClickBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = SauceTheme.colors.textSecondary
            )
        }
        BoundlessTextFieldBase(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(top = 5.dp),
            value = query,
            onValueChange = onQueryChanged,
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Поиск старта",
                    color = SauceTheme.colors.textMuted,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
        )
        if (query.isEmpty()) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Голосовой поиск",
                    tint = SauceTheme.colors.textMuted
                )
            }
        } else {
            IconButton(
                onClick = { onBrushClicked() }
            ) {
                Icon(

                    imageVector = Icons.Default.Close,
                    contentDescription = "Очистить поиск",
                    tint = SauceTheme.colors.textSecondary
                )
            }
        }
        IconButton(
            onClick = { onFilterClicked() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Notes,
                contentDescription = "Фильтры",
                tint = if (isWithFilter)
                    SauceTheme.colors.action
                else
                    SauceTheme.colors.textSecondary
            )
        }
    }
}
