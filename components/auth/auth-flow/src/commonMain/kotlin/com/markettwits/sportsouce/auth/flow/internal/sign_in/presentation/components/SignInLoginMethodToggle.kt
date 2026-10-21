package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod

@Composable
internal fun SignInLoginMethodToggle(
    modifier: Modifier = Modifier,
    currentLoginMethod: LoginMethod,
    onLoginMethodChange: (LoginMethod) -> Unit,
    enabled: Boolean = true,
) {
    val methods = LoginMethod.entries
    SauceSegmentedTabs(
        modifier = modifier.padding(9.dp),
        labels = methods.map { method ->
            when (method) {
                LoginMethod.PASSWORD -> "Пароль"
                LoginMethod.VK -> "ВКонтакте"
            }
        },
        selectedIndex = methods.indexOf(currentLoginMethod),
        enabled = enabled,
        onSelected = { onLoginMethodChange(methods[it]) },
    )
}
