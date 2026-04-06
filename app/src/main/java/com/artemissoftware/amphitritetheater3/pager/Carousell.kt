package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp as lerpDp
import androidx.compose.ui.util.lerp
import com.artemissoftware.amphitritetheater3.pager.Images.cardImages
import kotlin.math.absoluteValue

@Composable
fun Carousel(
    items: List<Int>,
    infiniteScroll: Boolean = true,
    modifier: Modifier = Modifier
) {


    val pagerState = getPagerState(infiniteScroll = infiniteScroll, size = items.size)

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1)
    )

    val focusedHeight = 479.dp
    val unfocusedHeight = 436.dp


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager (
            modifier = Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 44.dp),
            pageSpacing = 12.dp,
            beyondViewportPageCount = 2,
            flingBehavior = flingBehavior,
            key = { page -> page }
        ) { currentPage ->

            val currentIndex = getIndex(
                infiniteScroll = infiniteScroll,
                page = currentPage,
                size = items.size
            )

            val pageOffset = (
                    (pagerState.currentPage - currentPage) +
                            pagerState.currentPageOffsetFraction
                    ).absoluteValue

            val fraction = 1f - pageOffset.coerceIn(0f, 1f)

            // ✅ interpolate height using the Dp-specific lerp
            val height = lerpDp(unfocusedHeight, focusedHeight, fraction)

            PagerCard(
                title = currentPage.toString(),
                imageRes = items[currentIndex],
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - currentPage) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        val fraction = 1f - pageOffset.coerceIn(0f, 1f)

                        // scale
                        val scale = lerp(0.95f, 1f, fraction)
                        scaleX = scale
                        scaleY = scale

                        // alpha
                        alpha = lerp(0.3f, 1f, fraction)

                        // slight vertical shrink illusion
                        //translationY = lerp(40f, 0f, fraction)

                    }
                    .height(height)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun getPagerState(
    infiniteScroll: Boolean,
    size: Int
): PagerState {
    return if (infiniteScroll) {
        val fakePageCount = Int.MAX_VALUE
        val startIndex = fakePageCount / 2 - (fakePageCount / 2 % size)
        rememberPagerState(
            initialPage = startIndex,
            pageCount = { fakePageCount }
        )
    } else {
        rememberPagerState(
            initialPage = 0,
            pageCount = { size }
        )
    }
}

private fun getIndex(
    infiniteScroll: Boolean,
    page: Int,
    size: Int
): Int{
    return if(infiniteScroll) {
        page % size
    } else {
        page
    }
}

@Preview
@Composable
private fun CarouselPreview() {
    Carousel(
        items = cardImages
    )
}