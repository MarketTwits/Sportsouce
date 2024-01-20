package com.markettwits.profile.presentation.component.edit_profile.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(
    modifier: Modifier = Modifier,
    categories: List<String>,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // Category Field
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    label = {
                        Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp)
                    },
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        expanded = true
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(value) {
//                        if (value.isNotEmpty())
//                            focusRequester.requestFocus()
                    }
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (value.isNotEmpty()) {
                            items(
                                categories.filter {
                                    it.lowercase()
                                        .contains(value.lowercase()) || it.lowercase()
                                        .contains("others")
                                }.sorted()
                            ) {
                                CategoryItems(
                                    modifier = Modifier.focusRequester(focusRequester),
                                    title = it
                                ) { title ->
                                    onValueChange(title)
                                    expanded = false
                                }
                            }
                        } else {
                            items(
                                categories.sorted()
                            ) {
                                CategoryItems(
                                    title = it,
                                    modifier = Modifier.focusRequester(focusRequester),
                                ) { title ->
                                    onValueChange(title)
                                    expanded = false
                                }
                            }
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun CategoryItems(
    modifier: Modifier = Modifier,
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp, color = SportSouceColor.SportSouceBlue)
    }
}