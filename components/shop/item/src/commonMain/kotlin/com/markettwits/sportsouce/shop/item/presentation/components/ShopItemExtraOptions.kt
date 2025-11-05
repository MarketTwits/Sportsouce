package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShopItemExtraOptions(
    modifier: Modifier = Modifier,
    extraOption: List<ShopExtraOptions>,
    onClickOption: (String) -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
    ) {
        extraOption.forEach { options ->
            Text(
                text = options.title,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
            )
            FlowRow(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                options.items.forEach { (id, value, isSelected) ->
                    ShopItemExtraOptionButton(
                        modifier = Modifier.padding(4.dp),
                        isSelected = isSelected,
                        value = value,
                        onClick = { onClickOption(id) })
                }
            }
        }
    }
}

@Composable
private fun ShopItemExtraOptionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    value: String,
    onClick: () -> Unit,
) {
    val borderColors =
        if (isSelected) SportSouceColor.SportSouceLighBlue else MaterialTheme.colorScheme.outline

    val scale = animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "Button scale animation"
    )

    Button(
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .scale(scale.value),
        onClick = onClick,
        border = BorderStroke(if (isSelected) 3.dp else 1.dp, borderColors),
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
            fontSize = 14.sp,
            fontFamily = if (isSelected) FontNunito.bold() else FontNunito.semiBoldBold(),
        )
    }
}