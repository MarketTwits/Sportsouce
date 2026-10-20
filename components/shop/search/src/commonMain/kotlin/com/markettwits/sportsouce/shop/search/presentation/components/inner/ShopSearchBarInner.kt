package com.markettwits.sportsouce.shop.search.presentation.components.inner

import com.markettwits.core_ui.items.components.topbar.sauceTopBarContainerColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics


@Composable
internal fun ShopSearchBarInner(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChanged: (String) -> Unit,
    onBrushClicked: () -> Unit,
    onClickBack: () -> Unit,
    onApplyQuery: () -> Unit
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
                tint = SauceTheme.colors.textMuted
            )
        }
        BoundlessTextFieldBase(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(top = 5.dp).semantics { contentDescription = "Поиск товара" },
            value = query,
            onValueChange = onQueryChanged,
            maxLines = 1,
            minLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onNext = {
                onApplyQuery()
            }, onSearch = {
                onApplyQuery()
            }, onSend = {
                onApplyQuery()
            }
            ),
            placeholder = { Text("Поиск товара", color = SauceTheme.colors.textMuted,
                style = MaterialTheme.typography.bodyLarge, maxLines = 1) },
        )
        if (query.isNotEmpty()) {
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
    }
}
