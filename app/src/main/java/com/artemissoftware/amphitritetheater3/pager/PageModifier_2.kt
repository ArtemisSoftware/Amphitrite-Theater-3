package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.unit.lerp as lerpDp
import kotlin.math.absoluteValue

@Composable
fun pageModifier(
    numberOfItems: Int,
    pagerState: PagerState,
    currentPage: Int,
//    parentWidth: Dp,
//    horizontalPadding: Dp,
//    pageSpacing: Dp,
    focusedHeight: Dp,
    unfocusedHeight: Dp,
//    peekWidth: Dp = 0.dp
): Modifier {

    val pageOffset = (
            (pagerState.currentPage - currentPage) +
                    pagerState.currentPageOffsetFraction
            ).absoluteValue

    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
    val height = lerpDp(unfocusedHeight, focusedHeight, fraction)

    return when (numberOfItems) {
        1 -> {
            Modifier
                .fillMaxWidth()
                .height(height)
        }
        else -> {
            Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    val scale = lerp(0.95f, 1f, fraction)
                    //scaleX = scale
                    scaleY = scale
                    alpha = lerp(0.3f, 1f, fraction)
                }
                .height(height)
        }
    }
}


@Composable
fun rememberCarouselPageSize_2(
    itemsSize: Int,
    parentWidth: Dp,
    contentPadding: PaddingValues,
    pageSpacing: Dp,
    peekWidth: Dp = 16.dp
): PageSize {
    val layoutDirection = LocalLayoutDirection.current

    return remember(itemsSize, parentWidth, contentPadding, pageSpacing, peekWidth, layoutDirection) {
        when (itemsSize) {
            1 -> PageSize.Fill
            2 -> {
                val leftPadding = contentPadding.calculateLeftPadding(layoutDirection)
                val rightPadding = contentPadding.calculateRightPadding(layoutDirection)

                val cardWidth = parentWidth - leftPadding - rightPadding - pageSpacing - peekWidth
                PageSize.Fixed(cardWidth)
            }
            else -> PageSize.Fill
        }
    }
}