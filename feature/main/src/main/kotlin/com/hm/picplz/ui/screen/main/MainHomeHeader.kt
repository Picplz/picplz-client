package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.main.BuildConfig
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionExploreBottomSheet
import com.hm.picplz.ui.screen.main.search.SearchNavigateButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.core.ui.R as CoreR

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeSearchHeader(
    selectedRegion: String,
    onSearchClick: () -> Unit,
    onDevEntryClick: () -> Unit,
    onRegionSelected: (String) -> Unit,
) {
    var showRegionSheet by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { showRegionSheet = true },
                        onLongClick = if (BuildConfig.DEBUG) onDevEntryClick else null,
                    ),
        ) {
            Text(
                text = selectedRegion,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(CoreR.drawable.region_arrow_down),
                contentDescription = stringResource(R.string.main_region_select_content_description),
                modifier =
                    Modifier
                        .size(width = 12.dp, height = 10.dp)
                        .rotate(if (showRegionSheet) 180f else 0f),
            )
        }

        SearchNavigateButton(
            placeholder = stringResource(R.string.main_search_placeholder),
            onClick = onSearchClick,
        )
    }

    RegionExploreBottomSheet(
        visible = showRegionSheet,
        currentRegion = selectedRegion,
        onDismiss = { showRegionSheet = false },
        onApply = onRegionSelected,
    )
}
