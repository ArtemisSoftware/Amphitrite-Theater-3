package com.artemissoftware.amphitritetheater3.instazoom

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.artemissoftware.amphitritetheater3.Images.cardImages
import com.artemissoftware.amphitritetheater3.ui.theme.AmphitriteTheater3Theme
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val RELEASE_DURATION_MS = 300

/**
 * Pins the overlay popup to the window's top-left (0, 0) and lets it fill the whole
 * window. This is what lets the overlay sit ABOVE a Scaffold's top bar and stay
 * aligned with [ZoomState.bounds], which are reported in window coordinates.
 */
private val WindowOriginPositionProvider = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset = IntOffset.Zero
}

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
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ZoomOverlay(
    state: ZoomState,
    onReleaseFinished: () -> Unit
) {
    val density = LocalDensity.current
    val bounds = state.bounds

    // Animated mirror of the gesture. `translation` follows the finger pan only;
    // at rest it is Zero, which leaves the image sitting on its original card. The
    // image is NOT lifted to the screen centre — it zooms in place.
    val scale = remember { Animatable(state.scale) }
    val translation = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    // Dim the background more as the image grows; follows the animated scale so it
    // fades back out as the image returns home on release.
    val backgroundAlpha = ((scale.value - 1f) / 2f).coerceIn(0f, 0.8f)

    // Rendered in its own window-level layer so it draws on TOP of everything —
    // including a Scaffold's top bar — and is anchored to the window origin so the
    // window-coordinate `bounds` line up exactly with the original card.
    Popup(
        popupPositionProvider = WindowOriginPositionProvider,
        // Let the dimmed backdrop and the scaled image spill to the screen edges.
        properties = PopupProperties(clippingEnabled = false)
    ) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = backgroundAlpha))
    ) {
        val widthDp = with(density) { bounds.width.toDp() }
        val heightDp = with(density) { bounds.height.toDp() }

        // Live pinch: mirror the reported values instantly (no animation lag). The
        // image stays put on its card and only follows the finger pan.
        LaunchedEffect(state.scale, state.offset, state.releasing) {
            if (!state.releasing) {
                scale.snapTo(state.scale)
                translation.snapTo(state.offset)
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
