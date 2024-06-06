package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem

@Composable
internal fun StartConditionPanel(
    modifier: Modifier = Modifier,
    file: StartItem.ConditionFile,
    onClickFile: (String) -> Unit,
) {
    when (file) {
        is StartItem.ConditionFile.Base -> {
            StartContentBasePanel(modifier = modifier, label = "Положение") {
                StartConditionPanelContent(modifier, file, onClickFile)
            }
        }

        is StartItem.ConditionFile.Empty -> {}
    }
}

@Composable
private fun StartConditionPanelContent(
    modifier: Modifier = Modifier,
    file: StartItem.ConditionFile.Base,
    onClickFile: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                onClickFile(file.url)
            }
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            text = "Положение",
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp
        )
    }
}