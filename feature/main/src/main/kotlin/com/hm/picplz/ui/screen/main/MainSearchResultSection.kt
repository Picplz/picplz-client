package com.hm.picplz.ui.screen.main

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortFilterModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.core.ui.R as CoreR

private object SearchResultDefaults {
    val HorizontalPadding = 16.dp
    val SortTextIconGap = 4.dp
    val SortIconSize = 8.dp
}

@Composable
fun SearchResultSection(
    results: List<MainSearchPhotographerItem>,
    isLoading: Boolean,
    selectedSortType: SortType,
    onSortSelected: (SortType) -> Unit,
    onPhotographerClick: (MainSearchPhotographerItem) -> Unit,
) {
    var visibleSortFilter by remember { mutableStateOf(false) }
    val selectedSortLabel = stringResource(selectedSortType.labelResId)

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SortButton(
            label = selectedSortLabel,
            onClick = { visibleSortFilter = true },
            modifier = Modifier.padding(horizontal = SearchResultDefaults.HorizontalPadding),
        )

        if (isLoading) {
            SearchLoadingResult()
        } else if (results.isEmpty()) {
            SearchEmptyResult()
        } else {
            LazyColumn {
                itemsIndexed(
                    items = results,
                    key = { _, item -> item.id },
                ) { index, item ->
                    PhotographerListItem(
                        item = item,
                        onClick = { onPhotographerClick(item) },
                    )
                    if (index < results.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = SearchResultDefaults.HorizontalPadding),
                            color = MainThemeColor.Gray2,
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }

        SortFilterModalBottomSheet(
            onDismiss = { visibleSortFilter = false },
            visible = visibleSortFilter,
            onSelect = onSortSelected,
        )
    }
}

@Composable
private fun SortButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SearchResultDefaults.SortTextIconGap),
    ) {
        Text(
            text = label,
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray5,
        )
        Icon(
            painter = painterResource(id = CoreR.drawable.arrow_down),
            contentDescription = null,
            tint = MainThemeColor.Gray4,
            modifier = Modifier.size(SearchResultDefaults.SortIconSize),
        )
    }
}

@Composable
private fun SearchLoadingResult() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 163.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        CircularProgressIndicator(color = MainThemeColor.Black)
    }
}

private val SortType.labelResId: Int
    @StringRes
    get() =
        when (this) {
            SortType.POPULAR -> R.string.main_search_sort_popular
            SortType.RATING -> R.string.main_search_sort_rating
            SortType.FOLLOWER -> R.string.main_search_sort_follower
        }

@Composable
private fun SearchEmptyResult() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 163.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.main_search_no_result),
                style = MainThemeFont.TitleSmall,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = CoreR.drawable.user_undefined),
                contentDescription = stringResource(R.string.main_search_empty_image_content_description),
            )
        }
    }
}
