package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemissoftware.amphitritetheater3.pager.Images.cardImages

@Composable
fun Carousel(
    items: List<Int>,
    infiniteScroll: Boolean = true,
    modifier: Modifier = Modifier
) {

    val carouselConfig = rememberCarouselConfig(
        itemsSize = items.size,
        infiniteScroll = infiniteScroll
    )

    val pagerState = getPagerState(
        infiniteScroll = carouselConfig.infiniteScroll,
        size = items.size
    )

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1)
    )

    val focusedHeight = 479.dp
    val unfocusedHeight = 436.dp


    BoxWithConstraints(modifier = modifier) {
        val parentWidth = maxWidth
        val cardWidth = parentWidth - carouselConfig.contentPadding.calculateLeftPadding(LocalLayoutDirection.current) * 2 - carouselConfig.pageSpacing

        val dynamicPageSize_2 = when (items.size) {
            1 -> PageSize.Fill
            2 -> {
                val leftPadding = carouselConfig.contentPadding.calculateLeftPadding(LocalLayoutDirection.current)
                val rightPadding = carouselConfig.contentPadding.calculateRightPadding(LocalLayoutDirection.current)
                val peek = 16.dp // or whatever peek you want to show of next card

                val cardWidth = calculateTwoItemCardWidth(
                    parentWidth = parentWidth,
                    leftPadding = leftPadding,
                    rightPadding = rightPadding,
                    pageSpacing = carouselConfig.pageSpacing,
                    peekWidth = peek
                )

                PageSize.Fixed(cardWidth)
            }
            else -> PageSize.Fill
        }

        val dynamicPageSize = rememberCarouselPageSize(
            itemsSize = items.size,
            parentWidth = parentWidth,
            contentPadding = carouselConfig.contentPadding,
            pageSpacing = carouselConfig.pageSpacing,
            peekWidth = 16.dp // adjust peek width here
        )


        HorizontalPager (
            pageSize = dynamicPageSize,
            modifier = Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
            contentPadding = carouselConfig.contentPadding,
            pageSpacing = carouselConfig.pageSpacing,
            beyondViewportPageCount = carouselConfig.beyondViewportPageCount,
            flingBehavior = flingBehavior,
            userScrollEnabled = carouselConfig.userScrollEnabled,
            key = { page -> page }
        ) { currentPage ->

            val currentIndex = getIndex(
                infiniteScroll = carouselConfig.infiniteScroll,
                page = currentPage,
                size = items.size
            )



            val pagerModifier = rememberPageModifier(
                itemSize = items.size,
                pagerState = pagerState,
                currentPage = currentPage,
                focusedHeight = focusedHeight,
                unfocusedHeight = unfocusedHeight,
                pageSpacing = carouselConfig.pageSpacing,
                horizontalPadding = carouselConfig.contentPadding.calculateLeftPadding(LocalLayoutDirection.current),
                parentWidth = parentWidth
            )

            PagerCard(
                title = currentPage.toString() + " - " + cardWidth + " -- " + (340.dp),
                imageRes = items[currentIndex],
                modifier = pagerModifier
            )
        }
    }
}

fun calculateTwoItemCardWidth(
    parentWidth: Dp,
    leftPadding: Dp,
    rightPadding: Dp,
    pageSpacing: Dp,
    peekWidth: Dp
): Dp {
    return parentWidth - leftPadding - rightPadding - pageSpacing - peekWidth
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
private fun Carousel_one_card_Preview() {
    Carousel(
        modifier = Modifier.fillMaxSize(),
        items = cardImages.take(1)
    )
}

@Preview
@Composable
private fun Carousel_two_cards_Preview() {
    Carousel(
        modifier = Modifier.fillMaxSize(),
        items = cardImages.take(2)
    )
}

@Preview
@Composable
private fun CarouselPreview() {
    Carousel(
        modifier = Modifier.fillMaxSize(),
        items = cardImages
    )
}

