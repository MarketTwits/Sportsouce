package com.markettwits.start.presentation.start.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                StartFileContent(
                    modifier = modifier,
                    fileName = "Положение",
                    onClick = {
                        onClickFile(file.url)
                    }
                )
            }
        }

        is StartItem.ConditionFile.Empty -> {}
    }
}