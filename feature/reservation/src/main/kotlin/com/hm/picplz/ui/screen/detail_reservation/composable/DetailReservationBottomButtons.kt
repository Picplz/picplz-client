@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonBottomOutlinedButton
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

/**
 * 고객 예약 상세 하단 버튼.
 * - WAITING_APPROVAL: 채팅 바로가기 단독
 * - WAITING_SCHEDULE / RESERVED: 거래 완료하기(보조) + 채팅 바로가기(주)
 * - COMPLETED: 리뷰 쓰기(보조, 작성 완료 시 비활성) + 채팅 바로가기(주)
 */
@Composable
fun DetailReservationBottomButtons(
    currentReservationStatus: ReservationStatus,
    hasWrittenReview: Boolean,
    onChatClick: () -> Unit,
    onDealCompleteClick: () -> Unit,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (currentReservationStatus) {
        ReservationStatus.WAITING_APPROVAL -> {
            ChatButton(
                modifier = modifier,
                onClick = onChatClick,
            )
        }

        ReservationStatus.WAITING_SCHEDULE,
        ReservationStatus.RESERVED,
        -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                DealCompleteButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDealCompleteClick,
                )

                ChatButton(
                    modifier = Modifier.weight(1f),
                    onClick = onChatClick,
                )
            }
        }

        ReservationStatus.COMPLETED -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                ReviewButton(
                    modifier = Modifier.weight(1f),
                    enabled = !hasWrittenReview,
                    onClick = onReviewClick,
                )

                ChatButton(
                    modifier = Modifier.weight(1f),
                    onClick = onChatClick,
                )
            }
        }
    }
}

@Composable
private fun ChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_chat),
        onClick = onClick,
    )
}

@Composable
private fun DealCompleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomOutlinedButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_deal_complete),
        onClick = onClick,
    )
}

@Composable
private fun ReviewButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomOutlinedButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_review),
        enabled = enabled,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun DetailReservationBottomButtonsWaitingApprovalPreview() {
    DetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.WAITING_APPROVAL,
        hasWrittenReview = false,
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
    )
}

@Preview
@Composable
private fun DetailReservationBottomButtonsWaitingSchedulePreview() {
    DetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.WAITING_SCHEDULE,
        hasWrittenReview = false,
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
    )
}

@Preview
@Composable
private fun DetailReservationBottomButtonsReservedPreview() {
    DetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.RESERVED,
        hasWrittenReview = false,
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
    )
}

@Preview
@Composable
private fun DetailReservationBottomButtonsCompletedPreview() {
    DetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.COMPLETED,
        hasWrittenReview = false,
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
    )
}

@Preview
@Composable
private fun DetailReservationBottomButtonsCompletedReviewedPreview() {
    DetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.COMPLETED,
        hasWrittenReview = true,
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
    )
}
