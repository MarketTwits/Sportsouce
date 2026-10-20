package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ProfileLoadingContent() {
    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState())
        .semantics { contentDescription = "Загрузка профиля"; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate }) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        AdaptivePane(fillHeight = false) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                SauceSkeleton(Modifier.fillMaxWidth().height(164.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceSkeleton(Modifier.width(170.dp).height(24.dp))
                    SauceSkeleton(Modifier.fillMaxWidth().height(164.dp))
                    SauceSkeleton(Modifier.fillMaxWidth().height(112.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceSkeleton(Modifier.width(150.dp).height(24.dp))
                    SauceSkeleton(Modifier.fillMaxWidth().height(64.dp))
                }
            }
        }
    }
}
