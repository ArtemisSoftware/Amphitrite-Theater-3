package com.artemissoftware.amphitritetheater3.instazoom

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

/**
 * Snapshot of an in-progress pinch-zoom. It is produced by [InstaCard] while the
 * gesture is alive and consumed by [ZoomOverlay], which only mirrors it visually.
 *
 * @param bounds the original image position in window coordinates.
 * @param scale  accumulated pinch scale (>= 1f).
 * @param offset accumulated pan (finger movement) in pixels.
 */
data class ZoomState(
    @DrawableRes val imageRes: Int,
    val bounds: Rect,
    val scale: Float = 1f,
    val offset: Offset = Offset.Zero
)
