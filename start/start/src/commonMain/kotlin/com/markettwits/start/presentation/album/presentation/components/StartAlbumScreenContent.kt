package com.markettwits.start.presentation.album.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.Shapes

@Composable

private fun ImageCardContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: (String) -> Unit
) {

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize(),
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        },
        success = {
            SubcomposeAsyncImageContent(
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        onClick(imageUrl)
                    })
        },
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
    )
}

@Composable
fun StartAlbumScreenContentNew(
    modifier: Modifier = Modifier,
    items: List<String>
) {
    var fullImage by rememberSaveable {
        mutableStateOf("")
    }

    // State to track the currently loaded page
    var loadedPage by remember { mutableStateOf(1) }

    // State to track the items loaded so far
    var loadedItems by remember { mutableStateOf(items.take(10)) } // Initial load, adjust the number as needed

    val scrollableState = rememberLazyStaggeredGridState()


    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        state = scrollableState,
        columns = StaggeredGridCells.Adaptive(100.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(items) { photo ->
                ImageCardContent(imageUrl = photo) {
                    fullImage = photo
                }
            }
        },
    )

    if (fullImage.isNotEmpty()) {
        FullImageScreen(image = fullImage) {
            fullImage = ""
        }
    }

    LaunchedEffect(scrollableState) {
        val layoutInfo = scrollableState.layoutInfo
        snapshotFlow { layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItemInfo ->
                if (lastVisibleItemInfo != null && lastVisibleItemInfo.index >= loadedItems.lastIndex && loadedPage < items.size) {
                    loadedPage++
                    // Simulate loading the next page (replace this with your actual logic to load the next page)
                    loadedItems = items.take(loadedPage * 10)
                }
            }
    }
}