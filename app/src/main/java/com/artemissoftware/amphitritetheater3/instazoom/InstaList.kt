package com.artemissoftware.amphitritetheater3.instazoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.amphitritetheater3.Images.cardImages
import com.artemissoftware.amphitritetheater3.ui.theme.AmphitriteTheater3Theme

@Composable
fun InstaList() {
    var zoomState by remember { mutableStateOf<ZoomState?>(null) }

    InstaListContent(
        state = zoomState,
        onZoom = { zoomState = it },
        // Fingers lifted: keep the state but flag it, so the overlay can animate the
        // image home and fade the backdrop out instead of vanishing instantly.
        onZoomEnd = { zoomState = zoomState?.copy(releasing = true) },
        // Return-home animation finished: now the overlay can leave the tree.
        onReleaseFinished = { zoomState = null }
    )
}

@Composable
private fun InstaListContent(
    state: ZoomState?,
    onZoom: (ZoomState) -> Unit,
    onZoomEnd: () -> Unit,
    onReleaseFinished: () -> Unit
) {

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // Freeze the feed while a pinch is alive; restored when it ends.
            userScrollEnabled = state == null,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(cardImages) { imageRes ->
                InstaCard(
                    imageRes = imageRes,
                    modifier = Modifier.fillMaxWidth(),
                    onZoom = onZoom,
                    onZoomEnd = onZoomEnd
                )
            }
        }

        // Drawn on top of the list; present while a pinch is alive and during the
        // return-home animation that follows it.
        state?.let { ZoomOverlay(state = it, onReleaseFinished = onReleaseFinished) }
    }
}


@Preview
@Composable
private fun InstaListContentPreview() {
    AmphitriteTheater3Theme {
        InstaListContent(
            state = null,
            onZoom = {},
            onZoomEnd = {},
            onReleaseFinished = {}
        )
    }
}

@Preview
@Composable
private fun InstaListContent_in_zoom_Preview() {
    AmphitriteTheater3Theme {
        InstaListContent(
            state = ZoomState(
                imageRes = cardImages[0],
                bounds = Rect(
                    left = 16f,
                    top = 100f,
                    right = 384f,
                    bottom = 500f
                ),
                scale = 1.6f,
                offset = Offset.Zero
            ),
            onZoom = {},
            onZoomEnd = {},
            onReleaseFinished = {}
        )
    }
}
