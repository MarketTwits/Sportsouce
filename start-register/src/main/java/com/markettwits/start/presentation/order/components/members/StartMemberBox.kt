package com.markettwits.start.presentation.order.components.members

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartMemberBox(
    modifier: Modifier = Modifier,
    stage: String,
    memberName: String,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = SportSouceColor.VeryLighBlue),
        border = BorderStroke(2.dp, SportSouceColor.SportSouceBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stage,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
                Text(
                    text = memberName,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
            }
            Icon(
                imageVector = Icons.Default.ContactPage,
                contentDescription = memberName,
                tint = SportSouceColor.SportSouceBlue
            )
        }

    }
}