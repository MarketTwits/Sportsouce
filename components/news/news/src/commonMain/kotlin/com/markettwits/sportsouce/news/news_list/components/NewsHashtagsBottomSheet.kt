package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.news.common.model.NewsHashtag

@Composable
fun NewsHashtagsBottomSheet(
    hashtags: LazyPagingItems<NewsHashtag>,
    selectedHashtagId: Int?,
    onDismiss: () -> Unit,
    onToggleHashtag: (NewsHashtag) -> Unit,
    onClearAll: () -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val dismissSheet = {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
        onDismiss()
    }
    val normalizedQuery = searchQuery.trim()
    val loadedHashtags = hashtags.itemSnapshotList.items
    val shimmerChipWidths = remember {
        listOf(94.dp, 118.dp, 132.dp, 104.dp, 146.dp, 88.dp, 124.dp, 110.dp)
    }
    val visibleHashtags = remember(loadedHashtags, normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            loadedHashtags
        } else {
            loadedHashtags.filter { it.name.contains(normalizedQuery, ignoreCase = true) }
        }
    }

    DefaultModalBottomSheet(
        onDismissRequest = dismissSheet
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Хештеги",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )
            if (selectedHashtagId != null) {
                TextButton(onClick = onClearAll) {
                    Text(
                        text = "Сбросить",
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.regular(),
                        fontSize = 14.sp
                    )
                }
            }
        }
        OutlinedTextFieldBase(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            label = "Поиск хештега",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            },
            value = searchQuery,
            onValueChange = { searchQuery = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                },
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
            ),
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 480.dp)
        ) {
            val scrollState = rememberScrollState()
            LaunchedEffect(scrollState.value, scrollState.maxValue, hashtags.itemCount, normalizedQuery) {
                if (
                    normalizedQuery.isBlank()
                    && hashtags.itemCount > 0
                    && scrollState.maxValue > 0
                    && scrollState.value >= scrollState.maxValue - 120
                ) {
                    hashtags[hashtags.itemCount - 1]
                }
            }

            when (val refreshState = hashtags.loadState.refresh) {
                is LoadStateError -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = refreshState
                                .error
                                .mapToSauceError()
                                .mapToString(),
                            color = MaterialTheme.colorScheme.error,
                            fontFamily = FontNunito.medium(),
                            fontSize = 14.sp
                        )
                        TextButton(
                            modifier = Modifier,
                            onClick = { hashtags.refresh() }
                        ) {
                            Text(
                                text = "Повторить",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = FontNunito.bold(),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                is LoadStateLoading -> {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        repeat(12) { index ->
                            NewsHashtagShimmerItem(
                                modifier = Modifier.width(shimmerChipWidths[index % shimmerChipWidths.size])
                            )
                        }
                    }
                }

                else -> {
                    if (hashtags.itemCount == 0) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Хештеги не найдены",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.regular(),
                            fontSize = 14.sp
                        )
                    } else if (visibleHashtags.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "По запросу ничего не найдено",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.regular(),
                            fontSize = 14.sp
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            visibleHashtags.forEach { hashtag ->
                                FilterChipBase(
                                    selected = selectedHashtagId == hashtag.id,
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus(force = true)
                                        onToggleHashtag(hashtag)
                                        dismissSheet()
                                    },
                                    label = hashtag.name
                                )
                            }
                            if (hashtags.loadState.append is LoadStateLoading && normalizedQuery.isBlank()) {
                                repeat(4) { index ->
                                    NewsHashtagShimmerItem(
                                        modifier = Modifier.width(shimmerChipWidths[index % shimmerChipWidths.size])
                                    )
                                }
                            }
                            val appendState = hashtags.loadState.append
                            if (appendState is LoadStateError && normalizedQuery.isBlank()) {
                                Text(
                                    text = "Ошибка загрузки",
                                    color = MaterialTheme.colorScheme.error,
                                    fontFamily = FontNunito.medium(),
                                    fontSize = 14.sp
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    TextButton(
                                        onClick = { hashtags.retry() },
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                    ) {
                                        Text(
                                            text = "Повторить",
                                            color = MaterialTheme.colorScheme.error,
                                            fontFamily = FontNunito.medium(),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
