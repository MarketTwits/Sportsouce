package com.markettwits.start.presentation.order.components.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartMembers(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier.padding(5.dp),
            text = "Ваш заказ :",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        StartMemberBox(
            modifier = modifier.padding(5.dp),
            stage = "3 этап",
            memberName = "Иван Иванович"
        )
        StartMemberBox(
            modifier = modifier.padding(5.dp),
            stage = "3 этап",
            memberName = "Иван Иванович"
        )
        StartMemberBox(
            modifier = modifier.padding(5.dp),
            stage = "3 этап",
            memberName = "Иван Иванович"
        )
    }
}