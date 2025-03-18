package com.markettwits.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.filter.domain.models.ShopOptionInfo


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShopFilterOptions(
    options: List<ShopOptionInfo>,
    selectedOptions: List<ShopOptionInfo.Value>,
    onOptionClick: (ShopOptionInfo.Value) -> Unit,
) {
    Column {
        if (options.isNotEmpty()) {
            Text(
                text = "Опции",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                softWrap = true,
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        options.forEach { optionInfo ->
            Text(
                modifier = Modifier.padding(4.dp),
                text = optionInfo.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                softWrap = true,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.tertiary
            )
            optionInfo.values.let { values ->
                FlowRow(
                    maxItemsInEachRow = 4,
                ) {
                    values.forEach { value ->

                        val isSelected = selectedOptions.any { option -> option.uuid == value.uuid }

                        Button(
                            modifier = Modifier.padding(4.dp),
                            elevation = ButtonDefaults.buttonElevation(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                if (isSelected)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            shape = Shapes.medium,
                            onClick = { onOptionClick(value) }) {
                            Text(
                                text = value.name,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                softWrap = true,
                                textAlign = TextAlign.Start,
                                fontSize = 12.sp,
                                fontFamily = FontNunito.medium(),
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.onSecondary
                                else MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}


