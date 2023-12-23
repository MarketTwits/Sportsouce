package com.markettwits.profile.presentation.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MenuItemCard(modifier: Modifier = Modifier, item: MenuItem) {
    when (item) {
        is MenuItem.Base -> {
            Row(
                modifier = Modifier
                    .background(color = SportSouceColor.VeryLighBlue)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.Gray,
                    fontFamily = FontNunito.medium,
                  //  modifier = Modifier.padding(10.dp)
                )
            }
        }

        is MenuItem.Header -> {
            Row(
                modifier = Modifier
                    .background(color = SportSouceColor.VeryLighBlue)
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = item.title,
                    color = SportSouceColor.SportSouceBlue,
                    fontFamily = FontNunito.bold,
                  //  modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}