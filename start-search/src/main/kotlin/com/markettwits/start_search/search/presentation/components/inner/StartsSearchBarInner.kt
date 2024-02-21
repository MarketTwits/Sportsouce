package com.markettwits.start_search.search.presentation.components.inner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartsSearchBarInner(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChanged: (String) -> Unit,
    onBrushClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onClickBack: () -> Unit,
    onDoneClicked: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .clip(Shapes.extraLarge)
                .padding(5.dp)
                .clickable {
                    onClickBack()
                },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline
        )
        TextInputFieldOne(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp),
            height = 50.dp,
            onValueChange = {
                onQueryChanged(it)
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.outline),
            onDone = {
                onDoneClicked(query)
            },
            value = query,
            keyboardType = KeyboardType.Text,
            colors = Color.Gray,
            placeholder = {
                Text(
                    text = "Поиск старта",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
        )
        if (query.isEmpty()) {
            Icon(
                modifier = Modifier
                    .clip(Shapes.extraLarge)
                    .padding(5.dp),
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        } else {
            Icon(
                modifier = Modifier
                    .clip(Shapes.extraLarge)
                    .padding(5.dp)
                    .clickable {
                        onBrushClicked()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Gray
            )
        }
        Icon(
            modifier = Modifier
                .clip(Shapes.extraLarge)
                .padding(5.dp)
                .clickable {
                    onFilterClicked()
                },
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline
        )
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
        onDoneClicked = {}
    )
}