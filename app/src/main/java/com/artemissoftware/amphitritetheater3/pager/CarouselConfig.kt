package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.PageSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class CarouselConfig(
    val infiniteScroll: Boolean,
    val contentPadding: PaddingValues,
    val pageSpacing: Dp,
    val beyondViewportPageCount: Int,
    val userScrollEnabled: Boolean,
)

@Composable
fun rememberCarouselConfig(
    itemsSize: Int,
    infiniteScroll: Boolean,
): CarouselConfig {

    val (contentPadding, pageSpacing) = getPaddingAndSpacing(size = itemsSize)

    return CarouselConfig(
        infiniteScroll = getPagerInfiniteScroll(infiniteScroll = infiniteScroll, size = itemsSize),
        contentPadding = PaddingValues(horizontal = contentPadding),
        pageSpacing = pageSpacing,
        beyondViewportPageCount = getBeyondViewportPageCount(size = itemsSize),
        userScrollEnabled = itemsSize > 1,
    )
}

private fun getPagerInfiniteScroll(
    infiniteScroll: Boolean,
    size: Int
): Boolean {
    return infiniteScroll && size > 2
}

private fun getPaddingAndSpacing(
    size: Int
): Pair<Dp, Dp>{
    return when (size) {
        1 -> 16.dp to 0.dp
        2 -> 16.dp to 12.dp
        else -> 44.dp to 4.dp
    }
}

private fun getBeyondViewportPageCount(
    size: Int
): Int{
    return when {
        size <= 2 -> 0
        else -> 2
    }
}

@Composable
fun rememberCarouselPageSize(
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