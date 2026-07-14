package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.core.ui.R as CoreR

private object PhotographerListItemDefaults {
    val ItemVerticalPadding = 16.dp
    val ItemStartPadding = 16.dp
    val HeaderEndPadding = 16.dp
    val ThumbnailSize = 88.dp
    val ThumbnailRadius = 5.dp
    val ThumbnailTextGap = 10.dp
    val TitleAreaGap = 0.dp
    val TagHorizontalGap = 4.dp
    val TagListEndPadding = 16.dp
    val TagHorizontalPadding = 12.dp
    val TagVerticalPadding = 4.dp
    val TagRadius = 5.dp
    val ActiveDotSize = 10.dp
    val ActiveTextGap = 4.dp
    val HeaderActiveGap = 8.dp
}

@Composable
internal fun PhotographerListItem(
    item: MainSearchPhotographerItem,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    start = PhotographerListItemDefaults.ItemStartPadding,
                    top = PhotographerListItemDefaults.ItemVerticalPadding,
                    bottom = PhotographerListItemDefaults.ItemVerticalPadding,
                ),
        verticalAlignment = Alignment.Top,
    ) {
        PhotographerThumbnail(item = item)

        Spacer(modifier = Modifier.width(PhotographerListItemDefaults.ThumbnailTextGap))

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .height(PhotographerListItemDefaults.ThumbnailSize),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.padding(end = PhotographerListItemDefaults.HeaderEndPadding),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(PhotographerListItemDefaults.HeaderActiveGap),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(PhotographerListItemDefaults.TitleAreaGap),
                ) {
                    Text(
                        text = item.name,
                        style = MainThemeFont.BodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MainThemeColor.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (item.areaSummary.isNotBlank()) {
                        Text(
                            text = item.areaSummary,
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray4,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                if (item.isAvailableNow) {
                    ActiveStatus()
                }
            }

            PhotographerMoodTags(tags = item.moodTags)
        }
    }
}

@Composable
private fun PhotographerThumbnail(item: MainSearchPhotographerItem) {
    val shape = RoundedCornerShape(PhotographerListItemDefaults.ThumbnailRadius)

    Box(
        modifier =
            Modifier
                .size(PhotographerListItemDefaults.ThumbnailSize)
                .clip(shape)
                .background(MainThemeColor.Gray2, shape)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray2,
                    shape = shape,
                ),
    ) {
        AsyncImage(
            model = item.profileImageUri,
            placeholder = painterResource(id = CoreR.drawable.user_undefined),
            error = painterResource(id = CoreR.drawable.user_undefined),
            fallback = painterResource(id = CoreR.drawable.user_undefined),
            contentScale = ContentScale.Crop,
            contentDescription =
                stringResource(
                    R.string.main_search_photographer_profile_content_description,
                    item.name,
                ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun PhotographerMoodTags(tags: List<String>) {
    val displayTags =
        tags.mapNotNull { rawTag ->
            rawTag
                .trim()
                .takeUnless(String::isBlank)
                ?.let { tag -> if (tag.startsWith("#")) tag else "#$tag" }
        }.distinct()

    if (displayTags.isEmpty()) return

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(PhotographerListItemDefaults.TagHorizontalGap),
        contentPadding = PaddingValues(end = PhotographerListItemDefaults.TagListEndPadding),
    ) {
        items(displayTags) { tag ->
            Text(
                text = tag,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(PhotographerListItemDefaults.TagRadius))
                        .background(MainThemeColor.Gray1)
                        .border(
                            width = 1.dp,
                            color = MainThemeColor.Gray1,
                            shape = RoundedCornerShape(PhotographerListItemDefaults.TagRadius),
                        )
                        .padding(
                            horizontal = PhotographerListItemDefaults.TagHorizontalPadding,
                            vertical = PhotographerListItemDefaults.TagVerticalPadding,
                        ),
            )
        }
    }
}

@Composable
private fun ActiveStatus(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(PhotographerListItemDefaults.ActiveDotSize)
                    .clip(CircleShape)
                    .background(MainThemeColor.Green120),
        )
        Spacer(modifier = Modifier.width(PhotographerListItemDefaults.ActiveTextGap))
        Text(
            text = stringResource(R.string.main_search_result_active_status),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Green120,
        )
    }
}
