package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun FilterButtonSelectionPanel(modifier: Modifier = Modifier, onClickReset : () -> Unit, onClickApply : () -> Unit) {
    Row(modifier = modifier.padding(10.dp)) {
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp
            ),
            border = BorderStroke(0.5.dp, SportSouceColor.SportSouceBlue),
            onClick = { onClickReset() }
        ) {
            Text(
                text = "Сбросить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceBlue
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceBlue),
            border = BorderStroke(0.5.dp, SportSouceColor.SportSouceBlue),
            onClick = { onClickApply() }
        ) {
            Text(
                text = "Прмиенить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartMembersFilterButtonSelectionPanelPreview() {
    FilterButtonSelectionPanel(onClickApply = {}, onClickReset = {})
}