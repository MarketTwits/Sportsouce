package com.markettwits.start_search.search.presentation.components.inner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito


@Composable
fun StartsSearchBarInner(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChanged: (String) -> Unit,
    onBrushClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onClickBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = { onClickBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
        BoundlessTextFieldBase(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(top = 5.dp),
            value = query,
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Поиск старта",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.bold(),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
            onValueChange = {
                onQueryChanged(it)
            })
        if (query.isEmpty()) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            IconButton(
                onClick = { onBrushClicked() }
            ) {
                Icon(

                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        IconButton(
            onClick = { onFilterClicked() }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }

    }
}

@Preview
@Composable
private fun StartsSearchScreenPreview() {
    StartsSearchBarInner(
        onQueryChanged = {},
        onBrushClicked = {},
        onFilterClicked = {},
        onClickBack = {},
    )
}