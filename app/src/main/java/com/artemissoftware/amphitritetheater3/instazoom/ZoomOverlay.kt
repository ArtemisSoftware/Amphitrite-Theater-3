package com.artemissoftware.amphitritetheater3.instazoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.artemissoftware.amphitritetheater3.Images.cardImages
import com.artemissoftware.amphitritetheater3.ui.theme.AmphitriteTheater3Theme
import kotlin.math.roundToInt

/**
 * Pure visual mirror of an in-progress pinch. It owns no gesture state: it draws
 * [state]'s image and, while pinching, lifts it to the centre of the screen and
 * scales it around its own centre. When the pinch ends, the caller drops the state
 * and this whole composable leaves the tree — so the zoom "disappears" on release.
 */
@Composable
fun ZoomOverlay(state: ZoomState) {
    val density = LocalDensity.current
    val bounds = state.bounds

    // Dim the background more as the user zooms in further.
    val backgroundAlpha = ((state.scale - 1f) / 2f).coerceIn(0f, 0.8f)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = backgroundAlpha))
    ) {
        val widthDp = with(density) { bounds.width.toDp() }
        val heightDp = with(density) { bounds.height.toDp() }

        // Translation that moves the image's centre onto the screen centre, so the
        // zoom always appears centred regardless of where the card sat in the list.
        val screenCenter = Offset(constraints.maxWidth / 2f, constraints.maxHeight / 2f)
        val centerTranslation = screenCenter - bounds.center

        Image(
            painter = painterResource(state.imageRes),
            contentDescription = null,
            modifier = Modifier
                .offset {
                    IntOffset(bounds.left.roundToInt(), bounds.top.roundToInt())
                }
                .size(width = widthDp, height = heightDp)
                .graphicsLayer {
                    scaleX = state.scale
                    scaleY = state.scale
                    // Centre the image on screen, then let the pan follow the fingers.
                    translationX = centerTranslation.x + state.offset.x
                    translationY = centerTranslation.y + state.offset.y
                    // Grow from the image's own centre so it stays centred.
                    transformOrigin = TransformOrigin.Center
                }
        )
    }
}

@Preview
@Composable
private fun ZoomOverlayPreview() {
    AmphitriteTheater3Theme {
        ZoomOverlay(
            state = ZoomState(
                imageRes = cardImages[0],
                bounds = Rect(
                    left = 16f,
                    top = 100f,
                    right = 384f,
                    bottom = 500f
                ),
                scale = 1.8f,
                offset = Offset.Zero
            )
        )
    }
}
