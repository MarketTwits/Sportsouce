package com.markettwits.profile.presentation.sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun SignUpLabel(modifier: Modifier = Modifier, onClick : () -> Unit) {
    Row(
        modifier = modifier.clickable {
            onClick()
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colorStopsEnd = arrayOf(
            0.0f to SportSouceColor.SportSouceBlue,
            1f to Color.Transparent
        )
        val colorStopsStart = arrayOf(
            0.0f to Color.Transparent,
            1f to SportSouceColor.SportSouceBlue
        )
//        Box(
//            modifier = Modifier
//                .height(10.dp)
//                .background(Brush.horizontalGradient(colorStops = colorStopsStart))
//                .weight(1f)
//        )
        Text(
            text = "Зарегистрироваться",
            fontFamily = FontNunito.medium,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
//        Box(
//            modifier = Modifier
//                .height(10.dp)
//                .weight(1f)
//                .background(Brush.horizontalGradient(colorStops = colorStopsEnd))
//        )
    }
}

@Preview
@Composable
fun SignUpLabelPreview() {
    SignUpLabel{}
}