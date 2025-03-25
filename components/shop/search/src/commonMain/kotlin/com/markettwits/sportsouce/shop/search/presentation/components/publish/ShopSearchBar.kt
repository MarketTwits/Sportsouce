package com.markettwits.sportsouce.shop.search.presentation.components.publish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes


@Composable
fun ShopSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    isShowFilter: Boolean,
    onClickBack: () -> Unit,
    onClickSearchPanel: () -> Unit,
    onClickFilter: () -> Unit,
    onBrushClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = { onClickBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "ArrowBack",
                tint = MaterialTheme.colorScheme.outline
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = Shapes.large)
                .clip(Shapes.large),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .noRippleClickable(onClickSearchPanel),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск товара",
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = 5.dp,
                            vertical = 2.dp
                        )
                    )
                    if (query.isNotEmpty()) {
                        Text(
                            text = query,
                            fontSize = 16.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Text(
                            text = "Поиск товара",
                            fontSize = 16.sp,
                            fontFamily = FontNunito.bold(),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Row {
                    IconButton(
                        onClick = {
                            if (isShowFilter)
                                onClickFilter()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            tint = MaterialTheme.colorScheme.outline,
                            contentDescription = "Filter"
                        )
                    }
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onBrushClicked() }
                        ) {
                            Icon(

                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}