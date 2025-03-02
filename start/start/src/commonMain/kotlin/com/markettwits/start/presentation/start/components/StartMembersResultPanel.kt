package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.common.OnClick

@Composable
internal fun StartMembersResultPanel(
    modifier: Modifier = Modifier,
    membersResultCount: Int, onClick: OnClick
) {
    if (membersResultCount != 0) {
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
                    text = "Результаты старта",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.padding(start = 5.dp))
                Box(
                    modifier = Modifier
                        .clip(_root_ide_package_.com.markettwits.core_ui.items.components.Shapes.small)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(5.dp)
                ) {
                    Text(
                        text = membersResultCount.toString(),
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