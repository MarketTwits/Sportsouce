package com.markettwits.start_search.search.presentation.components.publish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito


@Composable
fun StartsSearchBarPublic(
    modifier: Modifier = Modifier,
    onClickSearchPanel: () -> Unit,
    onClickSettings: () -> Unit,
) {
    Row(
        modifier = modifier
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
                contentDescription = null
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