package com.flipperdevices.settings.impl.composable.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun SimpleElement(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    paddings: PaddingValues = PaddingValues(12.dp),
) {
    var rowModifier = Modifier
        .heightIn(min = 48.dp)
        .padding(paddings)
        .fillMaxWidth()
        .then(modifier)
    if (onClick != null) {
        rowModifier = Modifier
            .clickable(onClick = onClick)
            .then(rowModifier)
    }
    Row(
        rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            if (title != null) {
                Text(
                    text = title,
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            if (description != null) {
                Text(
                    text = description,
                )
            }
        }
    }
}

@Composable
internal fun SimpleElement(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int? = null,
    @StringRes descriptionId: Int? = null,
    onClick: (() -> Unit)? = null,
    paddings: PaddingValues = PaddingValues(12.dp),
) {
    val title: String? = titleId?.let { stringResource(id = it) }
    val description = descriptionId?.let { stringResource(id = it) }

    SimpleElement(
        modifier = modifier,
        title = title,
        description = description,
        onClick = onClick,
        paddings = paddings,
    )
}
