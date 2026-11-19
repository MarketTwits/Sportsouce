package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.topbar.sauceTopBarContainerColor
import com.attafitamim.krop.core.crop.CropState
import com.markettwits.core_ui.items.theme.SauceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropperTopBar(state: CropState) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = sauceTopBarContainerColor(),
            scrolledContainerColor = sauceTopBarContainerColor(),
            actionIconContentColor = SauceTheme.colors.textPrimary,
            navigationIconContentColor = SauceTheme.colors.textPrimary
        ),
        title = {
            Text(
                text = "Изменить фото профиля",
                style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = { state.done(accept = false) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Отмена")
            }
        },
        actions = {
            IconButton(onClick = { state.reset() }) {
                Icon(Icons.Default.Restore, "Сбросить кадрирование")
            }
            IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
                Icon(Icons.Default.Done, "Применить")
            }
        }
    )
}
