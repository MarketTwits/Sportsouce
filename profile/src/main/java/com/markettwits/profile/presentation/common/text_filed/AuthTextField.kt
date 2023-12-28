package com.markettwits.profile.presentation.common.text_filed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MyProfileTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = SportSouceColor.SportSouceBlue,
            focusedIndicatorColor = SportSouceColor.SportSouceBlue,
            unfocusedIndicatorColor = SportSouceColor.SportSouceBlue,
            errorIndicatorColor = SportSouceColor.SportSouceLightRed,
            errorContainerColor = Color.White,
            errorTextColor = SportSouceColor.SportSouceLightRed,
            focusedLabelColor = SportSouceColor.SportSouceBlue,
            unfocusedLabelColor = SportSouceColor.SportSouceBlue,
            focusedTextColor = SportSouceColor.SportSouceBlue,
            unfocusedTextColor = SportSouceColor.SportSouceBlue
        ),
        textStyle = TextStyle(fontFamily = FontNunito.medium),
        maxLines = 1,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}