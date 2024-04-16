package com.markettwits.selfupdater.components.selft_update.components.update_available

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun SelfUpdateButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClickStartUpdate: () -> Unit
) {
    ButtonContentBase(
        modifier = modifier.fillMaxWidth(),
        containerColor = SportSouceColor.SportSouceLightRed,
        textColor = Color.White,
        title = "Обновить приложение",
        onClick = { onClickStartUpdate() },
        showContent = isLoading,
        content = {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(24.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                strokeCap = StrokeCap.Round
            )
        })
}