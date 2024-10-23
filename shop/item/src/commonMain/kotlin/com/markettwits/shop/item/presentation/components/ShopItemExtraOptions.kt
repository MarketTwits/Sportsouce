package com.markettwits.shop.item.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.item.domain.models.ShopExtraOptions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShopItemExtraOptions(
    extraOption: List<ShopExtraOptions>,
    onClickOption: (String) -> Unit,
) {

    val state = remember(Unit) {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            slideInVertically { -it } togetherWith slideOutVertically { it }
        }
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
                FlowRow(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    options.items.forEach { (id, value, isSelected) ->
                        ShopItemExtraOptionButton(
                            isSelected = isSelected,
                            value = value,
                            onClick = { onClickOption(id) })
                    }
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
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
    Button(
        modifier = modifier.defaultMinSize(minHeight = 40.dp) // set to atleast 40dp
            .wrapContentHeight(Alignment.CenterVertically),
        onClick = onClick,
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