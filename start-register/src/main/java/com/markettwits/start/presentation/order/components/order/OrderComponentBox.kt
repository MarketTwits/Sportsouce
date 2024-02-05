package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrderComponentBox(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        OrderPriceInfo(modifier = modifier)
        OrderCheckRulesBox()
        OrderRegisterButton(modifier = modifier, isEnabled = true, isLoading = false, onClick = {})
    }
}