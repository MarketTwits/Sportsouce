package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.text.ExpandableHtmlText

@Composable
internal fun StartDescription(modifier: Modifier, description: String, isPartialData: Boolean = false) {
    ExpandableHtmlText(description, modifier, style = MaterialTheme.typography.bodyMedium)
}
