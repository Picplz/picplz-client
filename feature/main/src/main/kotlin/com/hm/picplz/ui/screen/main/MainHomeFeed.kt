package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
internal fun MainHomeFeed(
    items: List<CustomerHomeItem>,
    isLoadingMore: Boolean,
    hasNextPage: Boolean,
    loadMoreFailed: Boolean,
    onLoadNextPage: () -> Unit,
    onPortfolioVisible: (Long) -> Unit,
    onPhotographerClick: (Long) -> Unit,
    onReportClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: return@derivedStateOf false
            lastVisibleIndex >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadNextPage, hasNextPage, isLoadingMore, loadMoreFailed) {
        if (shouldLoadNextPage && hasNextPage && !isLoadingMore && !loadMoreFailed) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = items,
            key = { it.photographerId },
        ) { item ->
            CustomerHomeCard(
                item = item,
                onVisible = { onPortfolioVisible(item.photographerId) },
                onClick = { onPhotographerClick(item.photographerId) },
                onReportClick = onReportClick,
            )
        }

        if (isLoadingMore) {
            item(key = "home-feed-loading") {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = MainThemeColor.Black,
                    )
                }
            }
        }

        if (loadMoreFailed) {
            item(key = "home-feed-retry") {
                Text(
                    text = stringResource(R.string.main_feed_load_more_retry),
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onLoadNextPage)
                            .padding(vertical = 16.dp),
                )
            }
        }
    }
}
