package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Carousel(
    modifier: Modifier = Modifier
) {
    val state = rememberPagerState(
        initialPage = 0,
        pageCount = { Images.cardImages.size }
    )

    Box {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = state,
            // Provide a unique key for each page
            key = { page -> "page_$page" },
            snapPosition = SnapPosition.Start
        ) { page ->
            PagerCard(
                title = page.toString(),
                imageRes = Images.cardImages[page],
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CarouselPreview() {
    Carousel()
}