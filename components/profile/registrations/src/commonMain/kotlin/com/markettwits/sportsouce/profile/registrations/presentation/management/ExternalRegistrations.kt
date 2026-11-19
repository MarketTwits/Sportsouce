package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistration
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Intent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Sheet
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.State
import kotlinx.coroutines.flow.first

@Composable
internal fun ExternalRegistrationList(
    state: State,
    gridState: LazyGridState,
    onIntent: (Intent) -> Unit,
) {
    val entries = state.external.orEmpty()
    val add = { onIntent(Intent.Open(Sheet.External())) }
    if (state.externalHasMore && !state.externalLoading && state.externalError == null && entries.isNotEmpty()) {
        val nearEnd = entries.takeLast(2).map { it.id }
        val dispatch by rememberUpdatedState(onIntent)
        LaunchedEffect(gridState, nearEnd, state.externalNextOffset) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo.any { it.key in nearEnd } }.first { it }
            dispatch(Intent.ExternalList(more = true))
        }
    }
    SauceLazyVerticalGrid(
        columns = GridCells.Adaptive(320.dp * LocalDensity.current.fontScale),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
    ) {
        item(key = "external-header", span = { GridItemSpan(maxLineSpan) }) {
            when {
                state.external?.isEmpty() == true && state.externalError == null -> RegistrationPanel("Внешних регистраций пока нет") {
                    SheetText("Добавьте старт, на который зарегистрировались на другом сайте.")
                    SauceButton("Добавить регистрацию", onClick = add)
                }
                else -> SauceButton("Добавить регистрацию",
                    variant = SauceButtonVariant.Soft, onClick = add)
            }
        }
        if (state.externalLoading && state.external == null) item(key = "external-loading", span = { GridItemSpan(maxLineSpan) }) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        if (state.externalError != null && !state.externalRetryMore) item(key = "external-error", span = { GridItemSpan(maxLineSpan) }) {
            ExternalRegistrationListError(state.externalError) { onIntent(Intent.ExternalList()) }
        }
        items(entries, key = { it.id }) { entry ->
            ExternalRegistrationCard(entry, onIntent)
        }
        if (state.externalRetryMore) {
            when {
                state.externalError != null -> item(key = "external-page-error", span = { GridItemSpan(maxLineSpan) }) {
                    ExternalRegistrationListError(state.externalError) { onIntent(Intent.ExternalList(more = true)) }
                }
                state.externalLoading -> item(key = "external-page-loading", span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier.fillMaxWidth().padding(SauceSpacing.small), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExternalRegistrationListError(message: String, retry: () -> Unit) {
    RegistrationPanel("Не удалось загрузить регистрации") {
        InlineRegistrationError(message)
        SauceButton("Повторить", variant = SauceButtonVariant.Outlined, onClick = retry)
    }
}

@Composable
private fun ExternalRegistrationCard(entry: ExternalRegistration, accept: (Intent) -> Unit) {
    val colors = SauceTheme.colors
    val link = externalRegistrationUrl(entry.url)
    var showActions by remember { mutableStateOf(false) }
    Surface(Modifier.fillMaxWidth(), color = colors.surface, shape = SauceShapes.card) {
        Column {
            entry.poster?.url?.let { url ->
                AsyncImage(url, null, Modifier.fillMaxWidth().aspectRatio(16f / 9f)
                    .background(colors.surfaceSubtle), contentScale = ContentScale.Crop)
            }
            Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(entry.startName, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                    Box {
                        IconButton(onClick = { showActions = true }) {
                            Icon(Icons.Default.MoreVert, "Действия с регистрацией", tint = colors.textSecondary)
                        }
                        DropdownMenu(expanded = showActions, onDismissRequest = { showActions = false }) {
                            DropdownMenuItem(text = { Text("Редактировать") }, leadingIcon = { Icon(Icons.Default.Edit, null) }, onClick = {
                                showActions = false
                                accept(Intent.Open(Sheet.External(entry)))
                            })
                            DropdownMenuItem(text = { Text("Удалить", color = colors.danger.content) },
                                leadingIcon = { Icon(Icons.Default.DeleteOutline, null, tint = colors.danger.content) }, onClick = {
                                    showActions = false
                                    accept(Intent.Open(Sheet.Delete(entry)))
                                })
                        }
                    }
                }
                SheetText("${entry.distanceName} · ${displayRegistrationDate(entry.startDate.take(10))}")
                listOfNotNull(entry.ageGroup, entry.team).filter(String::isNotBlank).takeIf { it.isNotEmpty() }
                    ?.let { SheetText(it.joinToString(" · ")) }
                if (link != null) {
                    SauceButton("Открыть страницу старта", variant = SauceButtonVariant.Outlined,
                        onClick = { accept(Intent.OpenUrl(link)) })
                }
            }
        }
    }
}
