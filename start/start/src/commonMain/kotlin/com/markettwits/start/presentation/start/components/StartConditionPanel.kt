package com.markettwits.start.presentation.start.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
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
                    modifier = modifier.noRippleClickable {
                        onClickFile(file.url)
                    },
                    fileName = "Положение"
                )
            }
        }
        is StartItem.ConditionFile.Empty -> {}
    }
}