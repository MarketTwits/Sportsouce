package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.item.domain.models.ShopPageItem

@Composable
internal fun ShopItemExtraOptions(
    extraOption: List<ShopPageItem.ExtraOption>,
    onClickOption: (String) -> Unit,
) {
    Column {
        extraOption.forEach { options ->
            Text(
                text = options.title,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.items.forEach { (id, value, isSelected) ->
                    val borderColors =
                        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Button(
                        onClick = { onClickOption(id) },
                        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColors),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = Shapes.medium,
                        enabled = !isSelected
                    ) {
                        Text(
                            text = value,
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                        )
                    }
                }
            }
        }
    }
}