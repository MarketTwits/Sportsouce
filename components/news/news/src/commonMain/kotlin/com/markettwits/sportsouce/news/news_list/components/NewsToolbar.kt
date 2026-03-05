package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun NewsToolbar(
    selectedHashtagsCount: Int,
    onBack: () -> Unit,
    onOpenFilters: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 48.dp),
                    text = "Новости",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                BadgedBox(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    badge = {
                        if (selectedHashtagsCount > 0) {
                            Badge(
                                modifier = Modifier.offset(y = 4.dp)
                            ) {
                                Text(selectedHashtagsCount.toString())
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onOpenFilters) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтр хештегов",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}
