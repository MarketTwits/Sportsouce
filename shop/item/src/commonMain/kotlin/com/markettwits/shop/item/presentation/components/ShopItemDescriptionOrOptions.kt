package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.domain.model.ShopItem

@Composable
internal fun ShopItemDescriptionOrOptions(
    modifier: Modifier = Modifier,
    description: String,
    options: List<ShopItem.Option>,
) {
    var isDescription by remember {
        mutableStateOf(true)
    }

    Row(modifier = modifier.padding(vertical = 5.dp)) {
        DescriptionOrItemOptionsButton(isSelected = isDescription, title = "Описание", onClick = {
            isDescription = true
        })
        Spacer(Modifier.padding(5.dp))
        DescriptionOrItemOptionsButton(isSelected = !isDescription, title = "Опции", onClick = {
            isDescription = false
        })

    }
    if (isDescription) {
        ShopItemDescription(description)
    } else {
        ShopItemOptions(options)
    }
}

@Composable
private fun DescriptionOrItemOptionsButton(
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit,
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.tertiary

    ElevatedButton(
        onClick = onClick,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Text(
            text = title,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
        )
    }
}

@Composable
private fun ShopItemDescription(description: String) {
    HtmlText(
        text = description,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.medium(),
    )
}

@Composable
private fun ShopItemOptions(options: List<ShopItem.Option>) {
    Column {
        options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option.optionTitle,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = option.optionValue,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                )
            }
        }
    }
}