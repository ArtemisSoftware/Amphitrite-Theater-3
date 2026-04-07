package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemissoftware.amphitritetheater3.pager.Images.cardImages

@Composable
fun CustomCarousel(
    items: List<Int>,
    modifier: Modifier = Modifier
) {

    val carouselConfig = getCarouselConfig(
        numberOfItems = items.size,
        infiniteScroll = true
    )

    BoxWithConstraints(modifier = modifier) {

        val dynamicPageSize = rememberCarouselPageSize_2(
            itemsSize = items.size,
            parentWidth = maxWidth,
            contentPadding = carouselConfig.contentPadding,
            pageSpacing = carouselConfig.pageSpacing,
            peekWidth = 16.dp // adjust peek width here
        )

        BaseCarousel(
            pageSize = dynamicPageSize,
            carouselConfig = carouselConfig,
            modifier = Modifier.fillMaxWidth(),
            items = items,
            pagerSnapDistance = 1,
            content = { index, currentPage, pagerState ->

                val pageModifier = pageModifier(
                    numberOfItems = items.size,
                    pagerState = pagerState,
                    currentPage = currentPage,
                    focusedHeight = 479.dp,
                    unfocusedHeight = 436.dp,
                )

                PagerCard(
                    title = index.toString(),
                    imageRes = items[index],
                    modifier = pageModifier
                )
            }
        )
    }
}


private fun getCarouselConfig(
    numberOfItems: Int,
    infiniteScroll: Boolean
): CarouselConfig_2 {

    val (contentPadding, pageSpacing) = getPaddingAndSpacing(numberOfItems = numberOfItems)

    return CarouselConfig_2(
        numberOfItems = numberOfItems,
        infiniteScroll = getPagerInfiniteScroll(
            infiniteScroll = infiniteScroll,
            size = numberOfItems
        ),
        contentPadding = PaddingValues(horizontal = contentPadding),
        pageSpacing = pageSpacing,
        beyondViewportPageCount = getBeyondViewportPageCount(size = numberOfItems),
        userScrollEnabled = numberOfItems > 1,
    )
}

private fun getPagerInfiniteScroll(
    infiniteScroll: Boolean,
    size: Int
): Boolean {
    return infiniteScroll && size > 2
}

private fun getPaddingAndSpacing(
    numberOfItems: Int
): Pair<Dp, Dp>{
    return when (numberOfItems) {
        1 -> 16.dp to 0.dp
        2 -> 16.dp to 12.dp
        else -> 44.dp to 12.dp//44.dp to 4.dp
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


@Preview
@Composable
private fun CustomCarousel_one_card_Preview() {
    Box(modifier = Modifier.fillMaxSize()){
        CustomCarousel(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            items = cardImages.take(1)
        )
    }
}

@Preview
@Composable
private fun CustomCarousel_two_cards_Preview() {
    Box(modifier = Modifier.fillMaxSize()){
        CustomCarousel(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            items = cardImages.take(2)
        )
    }
}

@Preview
@Composable
private fun CustomCarouselPreview() {
    Box(modifier = Modifier.fillMaxSize()){

        CustomCarousel(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            items = cardImages
        )
    }
}