package com.markettwits.members.member_detail.presentation.components.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun MemberDetailButtons(
    modifier: Modifier = Modifier,
    deleteLoading: Boolean,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {
    Row(modifier = modifier) {
        ButtonContentBase(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
                .height(35.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiary,
            title = "Редактировать",
            onClick = { onClickEdit() }
        )
        ButtonContentBase(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
                .height(35.dp),
            containerColor = SportSouceColor.SportSouceLightRed,
            textColor = Color.White,
            title = "Удалить",
            onClick = { onClickDelete() },
            showContent = deleteLoading,
            content = {
                if (deleteLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(24.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        strokeCap = StrokeCap.Round
                    )
                }
            })
    }
}