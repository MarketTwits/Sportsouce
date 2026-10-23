package com.markettwits.sportsouce.edit_profile.sizes.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.AlertDialogScreen
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.edit_profile.change_password.presentation.component.SaveChangesButton
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizeField
import com.markettwits.sportsouce.edit_profile.sizes.presentation.component.ProfileSizesComponent
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.Intent

@Composable
fun ProfileSizesScreen(component: ProfileSizesComponent) {
    val state by component.state.collectAsState()
    ProfileSizesContent(state, component::obtainEvent)
}

@Composable
internal fun ProfileSizesContent(state: ProfileSizesStore.State, onEvent: (Intent) -> Unit) {
    val colors = SauceTheme.colors
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val snackbar = remember { SnackbarHostState() }
    var tone by remember { mutableStateOf(SauceSnackbarTone.Success) }
    fun save() { keyboard?.hide(); focus.clearFocus(); onEvent(Intent.Save) }

    SauceScaffold(
        topBar = { TopBarWithClip(title = "Размеры одежды и обуви", goBack = { onEvent(Intent.GoBack) }) },
        snackbarHost = { SauceSnackbarHost(snackbar, tone = tone) },
        bottomBar = {
            if (state.data != null || state.isLoading) AdaptivePane(modifier = Modifier.widthIn(max = 640.dp), fillHeight = false) {
                SaveChangesButton(Modifier.fillMaxWidth().imePadding().padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
                    loading = state.isSaving, enabled = state.hasChanges && !state.isSaving && !state.isLoading, onClick = ::save)
            }
        },
    ) { padding ->
        AdaptivePane(modifier = Modifier.widthIn(max = 640.dp)) {
            Column(Modifier.fillMaxWidth().padding(padding).consumeWindowInsets(padding)
                .sauceVerticalScroll(rememberScrollState()).padding(SauceSpacing.large),
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                when {
                    state.isLoading -> OnBackgroundCard {
                        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                            repeat(5) {
                                Box(Modifier.width(120.dp).height(16.dp).clip(SauceShapes.badge).background(colors.surfaceSubtle).shimmer())
                                Box(Modifier.fillMaxWidth().height(SauceSizes.fieldMinHeight).clip(SauceShapes.field).background(colors.surfaceSubtle).shimmer())
                            }
                        }
                    }
                    state.loadError != null -> OnBackgroundCard {
                        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                            Text("Не удалось загрузить размеры", style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                            Text(state.loadError, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                            SauceButton("Повторить", { onEvent(Intent.Retry) })
                        }
                    }
                    state.data != null -> {
                        Text("Необязательные предпочтения. Не связаны с магазином.",
                            style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                        OnBackgroundCard {
                            Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                                ProfileSizeField.entries.forEach { field ->
                                    val requester = remember(field) { FocusRequester() }
                                    val bringIntoView = remember(field) { BringIntoViewRequester() }
                                    LaunchedEffect(state.validationAttempt) {
                                        if (state.validationAttempt > 0 && state.errors.keys.firstOrNull() == field) {
                                            requester.requestFocus()
                                            bringIntoView.bringIntoView()
                                        }
                                    }
                                    SauceTextField(
                                        label = field.label(), value = state.data.value(field),
                                        onValueChange = { onEvent(Intent.Edit(field, it)) },
                                        enabled = !state.isSaving, placeholder = field.placeholder(),
                                        modifier = Modifier.focusRequester(requester).bringIntoViewRequester(bringIntoView),
                                        errorMessage = state.errors[field],
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = if (field == ProfileSizeField.Clothing) KeyboardType.Text else KeyboardType.Decimal,
                                            imeAction = if (field == ProfileSizeField.ShoeJp) ImeAction.Done else ImeAction.Next),
                                        keyboardActions = KeyboardActions(onNext = { focus.moveFocus(FocusDirection.Down) }, onDone = { save() }),
                                    )
                                }
                            }
                        }
                        if (state.hasChanges) SauceButton("Отменить изменения", { onEvent(Intent.Reset) },
                            variant = SauceButtonVariant.Text, enabled = !state.isSaving)
                    }
                }
            }
        }
    }
    if (state.confirmDiscard) AlertDialogScreen(
        title = "Выйти без сохранения?", text = "Изменённые размеры не сохранятся.",
        onDismissRequest = { onEvent(Intent.KeepEditing) }, onClickOk = { onEvent(Intent.Discard) },
    )
    EventEffect(event = state.event, onConsumed = { onEvent(Intent.ConsumeEvent) }) {
        tone = if (it.success) SauceSnackbarTone.Success else SauceSnackbarTone.Error
        snackbar.showSnackbar(it.message, withDismissAction = true)
    }
}

private fun ProfileSizeField.label(): String = when (this) {
    ProfileSizeField.Clothing -> "Размер одежды"
    ProfileSizeField.FootLength -> "Длина стопы, мм"
    ProfileSizeField.ShoeRu -> "Обувь RU"
    ProfileSizeField.ShoeUs -> "Обувь US"
    ProfileSizeField.ShoeJp -> "Обувь JP"
}

private fun ProfileSizeField.placeholder(): String = when (this) {
    ProfileSizeField.Clothing -> "M / 48"
    ProfileSizeField.FootLength -> "265"
    ProfileSizeField.ShoeRu -> "42"
    ProfileSizeField.ShoeUs -> "9.5"
    ProfileSizeField.ShoeJp -> "27"
}
