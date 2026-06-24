package com.artemissoftware.amphitritetheater3.instazoom

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.artemissoftware.amphitritetheater3.R
import com.artemissoftware.amphitritetheater3.ui.theme.AmphitriteTheater3Theme

@Composable
fun InstaCard(
    @DrawableRes imageRes: Int,
    onZoom: (ZoomState) -> Unit,
    onZoomEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Position of the image in window coordinates, captured on layout. The overlay
    // uses this to draw its copy exactly on top of the original.
    var bounds by remember { mutableStateOf(Rect.Zero) }

    Card {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .onGloballyPositioned { bounds = it.boundsInWindow() }
                    .pointerInput(imageRes) {
                        // The WHOLE pinch lives here, so there is no gesture hand-off.
                        // The overlay only mirrors what we report; on release it vanishes.
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)

                            var scale = 1f
                            var pan = Offset.Zero
                            var zooming = false

                            do {
                                val event = awaitPointerEvent()
                                val pressedCount = event.changes.count { it.pressed }

                                // Only react to a real pinch (2+ fingers). A single
                                // finger falls through so the LazyColumn can scroll.
                                if (pressedCount >= 2) {
                                    zooming = true
                                    scale *= event.calculateZoom()
                                    pan += event.calculatePan()

                                    onZoom(
                                        ZoomState(
                                            imageRes = imageRes,
                                            bounds = bounds,
                                            scale = scale.coerceAtLeast(1f),
                                            offset = pan
                                        )
                                    )

                                    event.changes.forEach {
                                        if (it.positionChanged()) it.consume()
                                    }
                                }
                            } while (event.changes.any { it.pressed })

                            if (zooming) onZoomEnd()
                        }
                    },
            )

            Text(
                text = LoremIpsum(10).values.joinToString(" "),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun InstaCardPreview() {
    AmphitriteTheater3Theme {
        InstaCard(
            imageRes = R.drawable.img_1,
            onZoom = {},
            onZoomEnd = {}
        )
    }
}
