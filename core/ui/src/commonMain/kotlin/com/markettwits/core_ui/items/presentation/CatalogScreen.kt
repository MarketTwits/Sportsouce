package com.markettwits.core_ui.items.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen() {
    CollapsingTopAppBar()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar() {
    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Заголовок")
            }
        }
    ) {
        LazyColumn {
            items(100) {
                AsyncImage(
                    model = "https://masterpiecer-images.s3.yandex.net/578a963b594a11ee927d363fac71b015:upscaled",
                    contentDescription = null
                )
                Text(text = it.toString(), modifier = Modifier.fillMaxWidth())
            }
        }
    }
}