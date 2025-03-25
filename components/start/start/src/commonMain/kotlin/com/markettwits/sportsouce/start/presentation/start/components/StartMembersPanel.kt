package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.presentation.common.OnClick


@Composable
internal fun StartMembersPanel(modifier: Modifier = Modifier, membersCount: Int, onClick: OnClick) {
    if (membersCount != 0) {
        HorizontalDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .noRippleClickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Список участников",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.padding(start = 5.dp))
                Box(
                    modifier = Modifier
                        .clip(Shapes.small)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(5.dp)
                ) {
                    Text(
                        text = membersCount.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.regular(),
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

