package com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.cropper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.mr0xf00.easycrop.CropState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CropperTopBar(state: CropState) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            actionIconContentColor = MaterialTheme.colorScheme.tertiary,
            navigationIconContentColor = MaterialTheme.colorScheme.tertiary
        ),
        title = {
            Text(
                text = "Изменить фото профиля",
                fontFamily = FontNunito.bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        navigationIcon = {
            IconButton(onClick = { state.done(accept = false) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = { state.reset() }) {
                Icon(Icons.Default.Restore, null)
            }
            IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
                Icon(Icons.Default.Done, null)
            }
        }
    )
}
