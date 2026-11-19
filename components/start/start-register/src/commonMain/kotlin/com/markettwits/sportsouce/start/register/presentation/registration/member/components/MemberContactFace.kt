package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow

@Composable
fun MemberContactFace(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    SauceSwitchRow(
        modifier = modifier.width(IntrinsicSize.Min),
        label = "Контактное лицо",
        checked = checked,
        onCheckedChange = onValueChanged,
    )
}
