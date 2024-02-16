package com.markettwits.start.presentation.member.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MemberContactFace(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Контактное лицо",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(
                checkedColor = SportSouceColor.SportSouceLighBlue,
                checkmarkColor = Color.White,
            ),
            onCheckedChange = {
                onValueChanged(it)
            })
    }
}