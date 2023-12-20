package com.markettwits.profile.presentation.component.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun ExitButton(modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.fillMaxWidth().padding(30.dp),
        onClick = { /*TODO*/ },
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceBlue,disabledContainerColor = SportSouceColor.SportSouceBlue)
    ){
        Text(
            text = "Выйти из аккаунта",
            color = Color.White,
            fontFamily = FontNunito.bold,
            //  modifier = Modifier.padding(10.dp)
        )
    }
}