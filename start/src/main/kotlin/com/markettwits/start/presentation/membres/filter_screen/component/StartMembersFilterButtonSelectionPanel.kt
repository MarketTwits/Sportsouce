package com.markettwits.start.presentation.membres.filter_screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start.presentation.common.OnClick

@Composable
fun StartMembersFilterButtonSelectionPanel(modifier: Modifier = Modifier, onClickReset : OnClick, onClickApply : OnClick) {
    Row(modifier = modifier.padding(10.dp)) {
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.tertiary),
            onClick = { onClickReset() }
        ) {
            Text(
                text = "Сбросить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            modifier = Modifier.weight(1f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.tertiary),
            onClick = { onClickApply() }
        ) {
            Text(
                text = "Прмиенить",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartMembersFilterButtonSelectionPanelPreview() {
    StartMembersFilterButtonSelectionPanel(onClickApply = {}, onClickReset = {})
}