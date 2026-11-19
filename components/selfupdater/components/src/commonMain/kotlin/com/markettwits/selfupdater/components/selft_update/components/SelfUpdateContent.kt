package com.markettwits.selfupdater.components.selft_update.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.NoOpImageTransformerImpl

@Suppress("UNUSED_PARAMETER")
@Composable
internal fun SelfUpdateContent(
    currentVersion: String,
    changes: String,
    isLoading: Boolean,
    isSuccess: Boolean,
    isFailed: Boolean,
    isUpdatesAvailable: Boolean,
    informationalOnly: Boolean,
    pendingMessage: String?,
    message: String,
    consumed: () -> Unit,
    onClickStartUpdate: () -> Unit,
    onClickCheck: () -> Unit,
    onClickGoBack: () -> Unit,
    downloadActive: Boolean = false,
    downloadPercent: Int? = null,
    downloadPaused: Boolean = false,
    transferPercent: Int? = null,
    readyToInstall: Boolean = false,
    onClickInstall: () -> Unit = {},
    installerOpening: Boolean = false,
    updateTarget: UpdateTarget = UpdateTarget.Android,
) {
    val desktop = updateTarget == UpdateTarget.Desktop
    val web = updateTarget == UpdateTarget.Web || informationalOnly
    val webOffer = web && isUpdatesAvailable && !isLoading && !isFailed && pendingMessage == null
    val colors = SauceTheme.colors
    val displayVersion = ReleaseVersion.fromTag(currentVersion)?.versionName ?: currentVersion
    val displayedPercent = if (downloadPaused) downloadPercent else transferPercent ?: downloadPercent
    val active = downloadActive || (isLoading && transferPercent != null)
    val title = when {
        isFailed -> if (web) "Не удалось проверить" else "Не удалось обновить"
        readyToInstall -> if (desktop) "Обновление загружено" else "Готово к установке"
        downloadPaused -> "Загрузка на паузе"
        active -> "Загрузка обновления"
        isLoading -> "Проверяем обновления…"
        pendingMessage != null -> "Состояние обновления"
        isUpdatesAvailable -> "Доступно обновление"
        else -> "Обновлений пока нет"
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val showSupportingText = maxHeight >= 600.dp && LocalDensity.current.fontScale <= 1.5f
        SauceScaffold(
            containerColor = colors.background,
            bottomBar = {
                Box(Modifier.fillMaxWidth().background(colors.background).navigationBarsPadding().imePadding(), contentAlignment = Alignment.Center) {
                    Column(Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(horizontal = SauceSpacing.extraLarge, vertical = SauceSpacing.large),
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                        when {
                            webOffer -> Text("Чтобы открыть новую версию веб-приложения, обновите страницу вручную.", modifier = Modifier.fillMaxWidth(),
                                color = colors.textSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                            installerOpening -> SauceButton(if (desktop) "Открываем файл…" else "Открываем установку…", {}, loading = true, variant = SauceButtonVariant.Success)
                            readyToInstall -> SauceButton(if (desktop) "Открыть файл обновления" else "Установить", onClickInstall, variant = SauceButtonVariant.Success)
                            downloadPaused -> SauceButton("Проверить ещё раз", onClickCheck, variant = SauceButtonVariant.Success)
                            active || isLoading -> SauceButton(if (active) displayedPercent?.let { "Загрузка $it%" } ?: "Загрузка обновления…" else "Проверяем обновления…", {}, loading = true, variant = SauceButtonVariant.Success)
                            pendingMessage == null && isUpdatesAvailable && !web -> SauceButton(if (desktop) "Скачать обновление" else "Обновить сейчас", onClickStartUpdate, variant = SauceButtonVariant.Success)
                            else -> SauceButton("Проверить ещё раз", onClickCheck, variant = SauceButtonVariant.Success)
                        }
                        if (showSupportingText && !web && (readyToInstall || active)) Text(
                            if (readyToInstall && desktop) "Откройте загруженный файл и установите новую версию приложения." else if (readyToInstall) "Установку подтвердит Android." else "Можно продолжать пользоваться приложением.",
                            modifier = Modifier.fillMaxWidth(), color = colors.textMuted, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                    }
                }
            },
        ) { padding ->
            BoxWithConstraints(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).padding(top = 56.dp), contentAlignment = Alignment.TopCenter) {
                val compact = maxHeight < 480.dp
                val showLogo = maxHeight >= 400.dp
                val heroSpace = (maxHeight * 0.06f).coerceIn(8.dp, 48.dp)
                Column(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth().sauceVerticalScroll(rememberScrollState())
                        .padding(horizontal = SauceSpacing.extraLarge, vertical = SauceSpacing.large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraLarge),
                ) {
                    if (showLogo) {
                        Spacer(Modifier.height(heroSpace))
                        Image(
                            painter = if (LocalDarkOrLightTheme.current) DefaultImages.SportSauceDarkLogo() else DefaultImages.SportSauceLightLogo(),
                            contentDescription = null, modifier = Modifier.size(if (compact) 80.dp else 144.dp), contentScale = ContentScale.Fit,
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                        Text(title, Modifier.fillMaxWidth().semantics { heading() }, color = colors.textPrimary,
                            style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                        if (displayVersion.isNotBlank()) Text("Новая версия $displayVersion", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            when {
                                active && transferPercent != null -> "Обновление загружается: $transferPercent%."
                                pendingMessage != null -> pendingMessage
                                isLoading -> "Получаем информацию о новой версии."
                                isUpdatesAvailable && web -> "Опубликована новая версия сайта."
                                isUpdatesAvailable -> "Своевременно обновляйте приложение, чтобы поддерживать стабильную работу."
                                else -> if (web) "У вас открыта актуальная версия сайта." else "У вас установлена актуальная версия приложения."
                            }, modifier = Modifier.fillMaxWidth(), color = colors.textSecondary, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                    }
                    if (active && (!downloadPaused || displayedPercent != null)) {
                        SauceLinearProgressIndicator("Прогресс загрузки", displayedPercent?.coerceIn(0, 100)?.div(100f))
                    }
                    if (changes.isNotBlank()) {
                        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                                Text("Что нового", Modifier.semantics { heading() }, color = colors.textPrimary, style = MaterialTheme.typography.titleMedium)
                                Box(Modifier.size(7.dp).clip(CircleShape).background(colors.success.content))
                            }
                            OnBackgroundCard {
                                Column(Modifier.padding(SauceSpacing.large)) {
                                    val handler = LocalUriHandler.current
                                    val safeHandler = remember(handler) {
                                        object : UriHandler { override fun openUri(uri: String) { if (isSafeReleaseNotesLink(uri)) handler.openUri(uri) } }
                                    }
                                    CompositionLocalProvider(LocalUriHandler provides safeHandler) {
                                        Markdown(content = changes,
                                            colors = markdownColor(text = colors.textPrimary, codeBackground = colors.surfaceSubtle, dividerColor = colors.border, tableBackground = colors.surface),
                                            typography = markdownTypography(
                                                h1 = MaterialTheme.typography.titleLarge,
                                                h2 = MaterialTheme.typography.titleMedium,
                                                h3 = MaterialTheme.typography.titleSmall,
                                                h4 = MaterialTheme.typography.titleSmall,
                                                h5 = MaterialTheme.typography.titleSmall,
                                                h6 = MaterialTheme.typography.titleSmall,
                                            ), imageTransformer = NoOpImageTransformerImpl(), components = markdownComponents(image = {}))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(SauceSpacing.large))
                }
            }
        }
        BackFloatingActionButton(modifier = Modifier.align(Alignment.TopStart).padding(start = SauceSpacing.extraSmall), back = onClickGoBack)
    }
}

internal fun isSafeReleaseNotesLink(uri: String): Boolean =
    uri.length <= 4096 && Regex("^https?://[^\\s/?#@]+(?:[/?#][^\\s]*)?$", RegexOption.IGNORE_CASE).matches(uri) &&
        uri.none { it.isISOControl() }
