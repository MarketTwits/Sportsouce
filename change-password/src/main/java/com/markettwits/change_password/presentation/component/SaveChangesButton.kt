package com.markettwits.change_password.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun SaveChangesButton(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        onClick = {
            onClick()
        },
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = SportSouceColor.SportSouceBlue,
            disabledContainerColor = SportSouceColor.SportSouceBlue
        )
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp), color = Color.White)
        } else {
            Text(
                text = "Сохранить изменения",
                color = Color.White,
                fontFamily = FontNunito.bold,
            )
        }
    }
}