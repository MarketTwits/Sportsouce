package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.theme.SauceSpacing
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Material field decoration without an editor: the anchor owns focus and activation. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OutlinedSelectorField(
    modifier: Modifier,
    selector: SelectorFieldPresentation,
    label: String,
    value: String,
    isError: Boolean,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    supportingText: (@Composable () -> Unit)?,
    placeholder: (@Composable () -> Unit)?,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    visualTransformation: VisualTransformation,
    errorMessage: String? = null,
) {
    SideEffect { selector.fieldValue?.value = value }
    val colors = defaultOutlineTextFiledColors()
    val renderedValue = remember(value, visualTransformation) {
        visualTransformation.filter(AnnotatedString(value)).text
    }
    val palette = SauceTheme.colors
    val focused by selector.interactionSource.collectIsFocusedAsState()
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = when {
            !selector.enabled -> palette.disabledContent
            isError -> palette.danger.content
            focused -> palette.action
            else -> palette.textSecondary
        })
        Box(Modifier.fillMaxWidth().heightIn(min = SauceSizes.fieldMinHeight)
            .semantics { if (isError) error(errorMessage ?: "Ошибка ввода") }, propagateMinConstraints = true) {
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = {
                    Text(renderedValue, style = MaterialTheme.typography.bodyLarge,
                        color = if (selector.enabled) SauceTheme.colors.textPrimary else SauceTheme.colors.disabledContent,
                        maxLines = if (singleLine) 1 else maxLines,
                        minLines = if (singleLine) 1 else minLines,
                        overflow = TextOverflow.Ellipsis)
                },
                enabled = selector.enabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = selector.interactionSource,
                isError = isError,
                label = null,
                placeholder = placeholder ?: selector.placeholder?.let { hint -> { Text(hint) } },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon ?: selector.trailingIcon,
                supportingText = supportingText,
                colors = colors,
                container = {
                    OutlinedTextFieldDefaults.Container(enabled = selector.enabled, isError = isError,
                        interactionSource = selector.interactionSource, colors = colors, shape = SauceShapes.field)
                },
            )
        }
    }
}
