package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun MenuTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    enabled : Boolean= true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        enabled = enabled,
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
            selectionColors = TextSelectionColors(SportSouceColor.SportSouceBlue,SportSouceColor.SportSouceBlue),
            focusedLabelColor = SportSouceColor.SportSouceBlue,
            unfocusedLabelColor = SportSouceColor.SportSouceBlue,
            focusedTextColor = SportSouceColor.SportSouceBlue,
            unfocusedTextColor = SportSouceColor.SportSouceBlue,
            disabledIndicatorColor = SportSouceColor.SportSouceBlue,
            disabledContainerColor = Color.White,
            disabledLabelColor = SportSouceColor.SportSouceBlue,
            disabledTextColor = SportSouceColor.SportSouceBlue
        ),
        textStyle = TextStyle(fontFamily = FontNunito.medium),
        maxLines = 1,
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}
@Composable
fun DateProfileTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick : () -> Unit,
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
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
        readOnly = true,
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onClick()
                        }
                    }
                }
            }
    )
}