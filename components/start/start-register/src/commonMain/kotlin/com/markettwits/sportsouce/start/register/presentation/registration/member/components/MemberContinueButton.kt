package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun MemberContinueButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickContinue: () -> Unit,
    onClickDisabled: (() -> Unit)? = null,
) {
    val buttonShape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Button(
            enabled = enabled,
            onClick = onClickContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.65f)
            ),
            shape = buttonShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "Продолжить",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontNunito.bold()
            )
        }

        if (!enabled && onClickDisabled != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .zIndex(1f)
                    .clip(buttonShape)
                    .clickable { onClickDisabled() }
            )
        }
    }
}
