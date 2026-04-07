package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue
import androidx.compose.ui.unit.lerp as lerpDp

@Composable
fun rememberPageModifier(
    itemSize: Int,
    pagerState: PagerState,
    currentPage: Int,
    focusedHeight: Dp,
    unfocusedHeight: Dp,
    pageSpacing: Dp,
    horizontalPadding: Dp,
    parentWidth: Dp
): Modifier {

    val pageOffset = (
            (pagerState.currentPage - currentPage) +
                    pagerState.currentPageOffsetFraction
            ).absoluteValue

    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
    val height = lerpDp(unfocusedHeight, focusedHeight, fraction)

    val density = LocalDensity.current

    return when (itemSize) {
        1 -> {
            Modifier
                .fillMaxWidth()
                .height(height)
        }
        2 -> {

            val parentWidthPx = with(density) { parentWidth.toPx() }
            val horizontalPaddingPx = with(density) { horizontalPadding.toPx() }
            val pageSpacingPx = with(density) { pageSpacing.toPx() }

            val cardWidthPx = parentWidthPx - horizontalPaddingPx * 2 - pageSpacingPx

            Modifier
                .graphicsLayer {
                    alpha = lerp(0.3f, 1f, fraction)
                }
                .width(with(density) { cardWidthPx.toDp() })
                .height(height)
        }
        else -> {
            Modifier
                .graphicsLayer {
                    val scale = lerp(0.95f, 1f, fraction)
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(0.3f, 1f, fraction)
                }
                .fillMaxWidth()
                .height(height)
        }
    }
}
