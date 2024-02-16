package com.markettwits.start.presentation.order.components.promo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun PromoBox(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = modifier.padding(10.dp)) {
                Icon(
                    imageVector = Icons.Default.WifiProtectedSetup,
                    contentDescription = "Промокод",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.padding(end = 5.dp))
                Text(
                    text = "Промокод",
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }
            Icon(
                modifier = Modifier
                    .clickable {},
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "back",
                tint = SportSouceColor.SportSouceBlue,
            )
        }
    }
}

@Preview
@Composable
private fun PromoBoxPreview() {
    PromoBox(onClick = {})
}