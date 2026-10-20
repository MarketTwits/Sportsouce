package com.markettwits.sportsouce.edit_profile.change_password.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
fun SaveChangesButton(modifier: Modifier = Modifier, loading: Boolean, enabled: Boolean = !loading, onClick: () -> Unit) {
    SauceButton("Сохранить", onClick, modifier = modifier, enabled = enabled, loading = loading)
}
