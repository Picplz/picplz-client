package com.hm.picplz.ui.screen.write_review.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.write_review.REVIEW_PHOTO_MAX_COUNT
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.core.ui.R as CoreUiR

private val photoTileSize = 120.dp
private val photoTileRadius = 5.dp
private val removeBadgeSize = 22.dp
private val removeIconSize = 10.dp
private const val REMOVE_BADGE_ALPHA = 0.6f

/**
 * 촬영 사진 첨부 영역. 좌측 고정 "사진 추가" 타일 + 첨부한 사진 썸네일 가로 스크롤.
 */
@Composable
fun ReviewPhotoAttachment(
    photoUris: List<String>,
    canAddPhoto: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.write_review_photo_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Text(
            text = stringResource(R.string.write_review_photo_subtitle),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray4,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
        )

        LazyRow(
            modifier = Modifier.padding(top = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (canAddPhoto) {
                item {
                    AddPhotoTile(
                        photoCount = photoUris.size,
                        onClick = onAddClick,
                    )
                }
            }

            items(photoUris, key = { it }) { uri ->
                ReviewPhotoThumbnail(
                    uri = uri,
                    onRemoveClick = { onRemoveClick(uri) },
                )
            }
        }
    }
}

@Composable
private fun AddPhotoTile(
    photoCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .size(photoTileSize)
                .clip(RoundedCornerShape(photoTileRadius))
                .background(MainThemeColor.Gray1)
                .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(CoreUiR.drawable.plus_icon),
            contentDescription = null,
            tint = MainThemeColor.Gray5,
        )

        Text(
            text = stringResource(R.string.write_review_photo_add),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray5,
            modifier = Modifier.padding(top = 8.dp),
        )

        Text(
            text = stringResource(R.string.write_review_photo_count_format, photoCount, REVIEW_PHOTO_MAX_COUNT),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray3,
        )
    }
}

@Composable
private fun ReviewPhotoThumbnail(
    uri: String,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(photoTileSize)) {
        AsyncImage(
            model = uri,
            contentDescription = stringResource(R.string.write_review_photo_thumbnail),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(photoTileRadius))
                    .background(MainThemeColor.Gray1),
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(removeBadgeSize)
                    .clip(CircleShape)
                    .background(MainThemeColor.Black.copy(alpha = REMOVE_BADGE_ALPHA))
                    .clickable(onClick = onRemoveClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.full_close),
                contentDescription = stringResource(R.string.write_review_photo_remove),
                tint = MainThemeColor.White,
                modifier = Modifier.size(removeIconSize),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewPhotoAttachmentPreviewEmpty() {
    PicplzTheme {
        ReviewPhotoAttachment(
            photoUris = emptyList(),
            canAddPhoto = true,
            onAddClick = {},
            onRemoveClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewPhotoAttachmentPreviewWithPhotos() {
    PicplzTheme {
        ReviewPhotoAttachment(
            photoUris = listOf("photo1", "photo2", "photo3"),
            canAddPhoto = true,
            onAddClick = {},
            onRemoveClick = {},
        )
    }
}
