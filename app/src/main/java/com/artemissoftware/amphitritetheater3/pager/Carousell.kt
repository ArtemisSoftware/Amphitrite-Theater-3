package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.artemissoftware.amphitritetheater3.R
import com.artemissoftware.amphitritetheater3.pager.Images.cardImages
import kotlin.math.absoluteValue

@Composable
fun Carousel(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { Images.cardImages.size }
    )

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager (
            modifier = Modifier
                .align(Alignment.Center),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 52.dp),
            beyondViewportPageCount = 2,
            flingBehavior = flingBehavior,
        ) { currentPage ->

            PagerCard(
                title = currentPage.toString(),
                imageRes = cardImages[currentPage],
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - currentPage) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

//                        lerp(
//                            start = 0.85f,
//                            stop = 1f,
//                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                        ).also { scale ->
//                            scaleX = scale
//                            scaleY = scale
//                        }
//
//                        alpha = lerp(
//                            start = 0.3f,
//                            stop = 1f,
//                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                        )

                        val fraction = 1f - pageOffset.coerceIn(0f, 1f)

                        // SCALE (more dramatic)
                        val scale = lerp(0.75f, 1f, fraction)
                        scaleX = scale
                        scaleY = scale

                        // ALPHA (clearly visible)
                        alpha = lerp(0.3f, 1f, fraction)

                        // OPTIONAL: slight vertical shrink illusion
                        translationY = lerp(40f, 0f, fraction)

                    }
                    .height(400.dp)
                    .fillMaxWidth()
            )

/*
            Column {
                Image(painter = painterResource(id = R.drawable.man_avater),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)).graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = (
                                    (pagerState.currentPage - currentPage) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue

                            // We animate the alpha, between 50% and 100%

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(
                                start = 1f,
                                stop = 1.75f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )

                        }.height(400.dp)
                        .fillMaxWidth()
                )
            }
        }
        */


        /*
        //Add a page indicator
        Row(modifier = Modifier.wrapContentHeight()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration){                    Color.DarkGray
                } else Color.LightGray
                Box(modifier = Modifier.padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp))
            }

        }
        */
    }
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                //pages = pages + "New Page: ${pages.size + 1}"
            }
        ) {
            Text("Add Page")
        }
}
}

@Preview
@Composable
private fun CarouselPreview() {
    Carousel()
}