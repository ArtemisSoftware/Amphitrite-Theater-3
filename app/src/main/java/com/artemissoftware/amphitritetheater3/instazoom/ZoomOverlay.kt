package com.artemissoftware.amphitritetheater3.instazoom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val RELEASE_DURATION_MS = 300

/**
 * Pure visual mirror of an in-progress pinch. It owns no gesture state: it draws
 * [state]'s image and, while pinching, lifts it to the centre of the screen and
 * scales it around its own centre.
 *
 * While [ZoomState.releasing] is false the overlay snaps to whatever the gesture
 * reports (instant, 1:1 with the fingers). Once the fingers lift the caller flips
 * `releasing`, and the overlay eases the scale and translation back to rest — i.e.
 * the image slides back onto its original card and the backdrop fades out — then
 * calls [onReleaseFinished] so the caller can drop the state and leave the tree.
 */
@Composable
fun ZoomOverlay(
    state: ZoomState,
    onReleaseFinished: () -> Unit
) {
    val density = LocalDensity.current
    val bounds = state.bounds

    // Animated mirror of the gesture. `translation` is the full lift (centre the
    // image on screen + follow the pan); at rest it is Zero, which leaves the image
    // sitting exactly on its original card.
    val scale = remember { Animatable(state.scale) }
    val translation = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    // Dim the background more as the image grows; follows the animated scale so it
    // fades back out as the image returns home on release.
    val backgroundAlpha = ((scale.value - 1f) / 2f).coerceIn(0f, 0.8f)

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

        // Live pinch: mirror the reported values instantly (no animation lag).
        LaunchedEffect(state.scale, state.offset, centerTranslation, state.releasing) {
            if (!state.releasing) {
                scale.snapTo(state.scale)
                translation.snapTo(centerTranslation + state.offset)
            }
        }

        // Release: ease everything back to rest, then let the caller drop the state.
        LaunchedEffect(state.releasing) {
            if (state.releasing) {
                listOf(
                    launch { scale.animateTo(1f, tween(RELEASE_DURATION_MS)) },
                    launch { translation.animateTo(Offset.Zero, tween(RELEASE_DURATION_MS)) }
                ).joinAll()
                onReleaseFinished()
            }
        }

        Image(
            painter = painterResource(state.imageRes),
            contentDescription = null,
            modifier = Modifier
                .offset {
                    IntOffset(bounds.left.roundToInt(), bounds.top.roundToInt())
                }
                .size(width = widthDp, height = heightDp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    translationX = translation.value.x
                    translationY = translation.value.y
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
            ),
            onReleaseFinished = {}
        )
    }
}
