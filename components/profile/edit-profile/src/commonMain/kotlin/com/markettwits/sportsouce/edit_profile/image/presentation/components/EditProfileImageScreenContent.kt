package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.dialog.SauceDialogContent
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.delay

@Composable
fun EditProfileImageScreenContent(
    state: EditProfileImageStore.State,
    dismiss: () -> Unit,
    onClickImageBox: () -> Unit,
) {
    val colors = SauceTheme.colors
    SauceDialogContent(
        title = "Изменить фото профиля",
        actions = { SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Text) },
    ) {
        Surface(
            onClick = onClickImageBox,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp).semantics { role = Role.Button },
            shape = SauceShapes.field,
            color = colors.surfaceSubtle,
            contentColor = colors.textSecondary,
            border = BorderStroke(1.dp, colors.border),
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (state.isLoading) CircularProgressIndicatorBase()
                else InBoxImageContent()
            }
        }
        if (state.message.isNotBlank()) Text(state.message,
            style = MaterialTheme.typography.bodySmall,
            color = if (state.isFailed) colors.danger.content else colors.textSecondary)
    }
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            delay(800)
            dismiss()
        }
    }
}
