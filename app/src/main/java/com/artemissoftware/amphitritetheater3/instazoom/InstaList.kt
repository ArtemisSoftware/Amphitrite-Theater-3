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
        onZoomEnd = { zoomState = null }
    )
}

@Composable
private fun InstaListContent(
    state: ZoomState?,
    onZoom: (ZoomState) -> Unit,
    onZoomEnd: () -> Unit
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

        // Drawn on top of the list; present only while a pinch is alive.
        state?.let { ZoomOverlay(state = it) }
    }
}


@Preview
@Composable
private fun InstaListContentPreview() {
    AmphitriteTheater3Theme {
        InstaListContent(
            state = null,
            onZoom = {},
            onZoomEnd = {}
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
            onZoomEnd = {}
        )
    }
}
