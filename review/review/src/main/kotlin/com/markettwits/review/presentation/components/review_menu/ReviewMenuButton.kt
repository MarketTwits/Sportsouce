package com.markettwits.review.presentation.components.review_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun ReviewMenuButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    background: Color,
    fontColor: Color,
    onClick : () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .clip(Shapes.large)
            .clickable(onClick = onClick::invoke)
            .background(background)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = fontColor)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = title,
                color = fontColor,
                maxLines = 1,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontNunito.bold
            )
        }
    }
}

@Preview
@Composable
fun ReviewMenuButtonPreview() {
    ReviewMenuButton(
        title = "Популярное",
        icon = Icons.Filled.Newspaper,
        background = Color.Green,
        fontColor = Color.Black,
        onClick = {}
    )
}