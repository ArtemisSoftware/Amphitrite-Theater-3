package com.artemissoftware.amphitritetheater3.pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CarouselConfig_2(
    val numberOfItems: Int,
    val infiniteScroll: Boolean = false,
    val contentPadding: PaddingValues = PaddingValues(),
    val pageSpacing: Dp = 0.dp,
    val beyondViewportPageCount: Int = 0,
    val userScrollEnabled: Boolean = true,
)
