package com.hm.picplz.ui.screen.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
internal fun CustomerHomePortfolioIndicator(pagerState: PagerState) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        repeat(pagerState.pageCount) { page ->
            val dotSize by
                animateDpAsState(
                    targetValue =
                        portfolioIndicatorDotSize(
                            page = page,
                            currentPage = pagerState.currentPage,
                            pageCount = pagerState.pageCount,
                        ),
                    animationSpec = tween(durationMillis = 200),
                    label = "portfolioIndicatorDotSize",
                )
            val dotColor by
                animateColorAsState(
                    targetValue =
                        if (pagerState.currentPage == page) {
                            MainThemeColor.Black
                        } else {
                            MainThemeColor.Gray2
                        },
                    animationSpec = tween(durationMillis = 200),
                    label = "portfolioIndicatorDotColor",
                )
            Box(
                modifier = Modifier.size(6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(dotColor),
                )
            }
        }
    }
}

private fun portfolioIndicatorDotSize(
    page: Int,
    currentPage: Int,
    pageCount: Int,
) = when {
    currentPage == 0 && page <= 2 -> 6.dp
    currentPage == 0 && page == 3 -> 4.dp
    currentPage == pageCount - 1 && page >= pageCount - 3 -> 6.dp
    currentPage == pageCount - 1 && page == pageCount - 4 -> 4.dp
    page == currentPage -> 6.dp
    page == currentPage - 1 || page == currentPage + 1 -> 4.dp
    else -> 2.dp
}
