package com.markettwits.sportsouce.profile.authorized.receipts

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal actual fun ReceiptDocument(html: String, modifier: Modifier) {
    val colors = rememberReceiptDocumentColors()
    val document = remember(html, colors) { receiptDisplayHtml(html, colors) }
    // Keep one callback state across theme changes; the existing WebView is retained.
    var renderedDocument by remember { mutableStateOf<String?>(null) }
    val ready = renderedDocument == document
    Box(modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context -> WebView(context).apply {
                // Haze records the parent into an offscreen layer. Chromium's hardware tiles
                // continuously invalidate inside that layer; this static document uses a stable
                // software layer instead of competing with the glass compositor.
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                setBackgroundColor(colors.background.toArgb())
                settings.apply {
                    javaScriptEnabled = false
                    allowFileAccess = false
                    allowContentAccess = false
                    blockNetworkLoads = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String?) {
                        val pendingDocument = view.tag as? String
                        // Page completion alone does not guarantee that Chromium painted it.
                        view.postVisualStateCallback(0L, object : WebView.VisualStateCallback() {
                            override fun onComplete(requestId: Long) {
                                if (view.tag == pendingDocument) renderedDocument = pendingDocument
                            }
                        })
                    }
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = true
                    @Deprecated("Legacy WebView callback")
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?) = true
                }
            } },
            update = { view ->
                view.setBackgroundColor(colors.background.toArgb())
                if (view.tag != document) {
                    view.tag = document
                    view.loadDataWithBaseURL(null, document, "text/html", "UTF-8", null)
                }
            },
            onRelease = { view -> view.stopLoading(); view.destroy() },
        )
        if (!ready) Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
            ReceiptSkeletons(document = true)
        }
    }
}
