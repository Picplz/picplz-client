@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily.bodyLarge
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont.Body

/**
 * 고객·작가 예약 상세가 공유하는 정보 섹션.
 *
 * [packageName]/[place] 는 작가 화면만 실데이터를 넘깁니다.
 * 고객 화면은 예약 상세 조회 API가 작가 전용이라 아직 값을 구할 수 없어 기본값(더미)을 씁니다.
 */
@Composable
fun ReservationInfoSection(
    modifier: Modifier = Modifier,
    customerName: String = "",
    packageName: String = DUMMY_PACKAGE_NAME,
    place: String = DUMMY_PLACE,
    shootingDateText: String = "작가와 협의",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (customerName.isNotBlank()) {
            ReservationInfoItem(
                title = "고객명",
                description = customerName,
            )
        }
        ReservationInfoItem(
            title = "촬영 상품명",
            description = packageName.ifBlank { DUMMY_PACKAGE_NAME },
        )
        ReservationInfoItem(
            title = "촬영 장소",
            description = place.ifBlank { DUMMY_PLACE },
        )
        ReservationInfoItem(
            title = "촬영 일시",
            description = shootingDateText,
        )
    }
}

private const val DUMMY_PACKAGE_NAME = "프로필 패키지"
private const val DUMMY_PLACE = "서울특별시 종로구 효자로 3, 네번째 테이블 창문 앞"

@Composable
private fun ReservationInfoItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ReservationInfoItemTitle(title = title)
        ReservationInfoItemDescription(description = description)
    }
}

@Composable
private fun ReservationInfoItemTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        color = Color.Black,
    )
}

@Composable
private fun ReservationInfoItemDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        modifier = modifier,
        style = Body,
        color = MainThemeColor.Gray5,
    )
}

@Preview
@Composable
private fun ReservationInfoItemPreview() {
    ReservationInfoSection()
}
