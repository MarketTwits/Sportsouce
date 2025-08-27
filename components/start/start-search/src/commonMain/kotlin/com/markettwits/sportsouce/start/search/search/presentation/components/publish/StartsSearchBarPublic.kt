package com.markettwits.sportsouce.start.search.search.presentation.components.publish

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes


@Composable
fun StartsSearchBarPublic(
    modifier: Modifier = Modifier,
    onClickSearchPanel: () -> Unit,
    onClickSettings: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .weight(0.8f)
                .shadow(elevation = 2.dp, shape = Shapes.large)
                .clip(Shapes.large),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            SearchPanel(onClick = onClickSearchPanel)
        }
        IconButton(
            modifier = Modifier.weight(0.2f),
            onClick = onClickSettings
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
private fun SearchPanel(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Поиск старта",
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = "Поиск старта",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}