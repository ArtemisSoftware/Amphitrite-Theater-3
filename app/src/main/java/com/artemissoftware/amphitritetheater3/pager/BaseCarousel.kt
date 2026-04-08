package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.amphitritetheater3.pager.Images.cardImages

@Composable
fun BaseCarousel(
    items: List<Int>,
    content: @Composable (index: Int, currentPage: Int, pagerState: PagerState) -> Unit,
    modifier: Modifier = Modifier,
    carouselConfig: CarouselConfig = CarouselConfig(numberOfItems = items.size),
    pageSize: PageSize = PageSize.Fill,
    pagerSnapDistance: Int = 0
) {

    val pagerState = getPagerState(
        infiniteScroll = carouselConfig.infiniteScroll,
        size = items.size
    )

    val flingBehavior = if(pagerSnapDistance != 0) {
        PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(pagerSnapDistance)
        )
    } else PagerDefaults.flingBehavior(state = pagerState)

    HorizontalPager(
        modifier = modifier,
        pageSize = pageSize,
        verticalAlignment = Alignment.CenterVertically,
        state = pagerState,
        flingBehavior = flingBehavior,
        contentPadding = carouselConfig.contentPadding,
        pageSpacing = carouselConfig.pageSpacing,
        beyondViewportPageCount = carouselConfig.beyondViewportPageCount,
        userScrollEnabled = carouselConfig.userScrollEnabled,
        key = { page -> page }
    ){ currentPage ->

        val currentIndex = getIndex(
            infiniteScroll = carouselConfig.infiniteScroll,
            page = currentPage,
            size = items.size
        )


        content(
            currentIndex,
            currentPage,
            pagerState
        )
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
private fun BaseCarouselPreview() {
    BaseCarousel(
        modifier = Modifier.fillMaxSize(),
        items = cardImages,
        content = { currentIndex, currentPage, pagerState ->

            Image(
                painter = painterResource(id = cardImages[currentIndex]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

        }
    )
}
