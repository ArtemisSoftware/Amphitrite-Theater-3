package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Carousel(
    modifier: Modifier = Modifier
) {
    Box {
        // Define the number of pages (e.g., 3)
        val state = rememberPagerState { Images.cardImages.size }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) { page ->
            PagerCard(
                title = page.toString(),
                imageRes = Images.cardImages[page],
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
private fun CarouselPreview() {
    Carousel()
}