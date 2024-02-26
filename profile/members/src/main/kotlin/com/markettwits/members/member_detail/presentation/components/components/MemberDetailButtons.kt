package com.markettwits.members.member_detail.presentation.components.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MemberDetailButtons(
    modifier: Modifier = Modifier,
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
            onClick = { onClickEdit() }
        )
    }
}