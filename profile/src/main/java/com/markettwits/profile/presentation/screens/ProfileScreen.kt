package com.markettwits.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.profile.presentation.component.ProfileScreenContent
import com.markettwits.profile.presentation.component.menu.ExitButton
import com.markettwits.profile.presentation.component.top_bar.ProfileTopBar

@Composable
fun ProfileScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            ProfileTopBar()
            ProfileScreenContent()
            ExitButton()
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}