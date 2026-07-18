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
 * 작가 예약 상세 하단 버튼.
 * - WAITING_APPROVAL: 화면에서 예약 승인 버튼(ReservationApproveButton)으로 대체됨
 * - WAITING_SCHEDULE: 채팅 바로가기 단독
 * - RESERVED: 거래 완료하기(보조) + 채팅 바로가기(주)
 * - COMPLETED: 채팅 바로가기 단독
 */
@Composable
fun PhotographerDetailReservationBottomButtons(
    currentReservationStatus: ReservationStatus,
    onChatClick: () -> Unit,
    onDealCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (currentReservationStatus) {
        ReservationStatus.WAITING_APPROVAL,
        ReservationStatus.WAITING_SCHEDULE,
        ReservationStatus.COMPLETED,
        -> {
            ChatButton(
                modifier = modifier,
                onClick = onChatClick,
            )
        }

        ReservationStatus.RESERVED -> {
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
    }
}

@Composable
fun ReservationApproveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_reservation_approve),
        onClick = onClick,
    )
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

@Preview
@Composable
private fun PhotographerDetailReservationBottomButtonsWaitingSchedulePreview() {
    PhotographerDetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.WAITING_SCHEDULE,
        onChatClick = {},
        onDealCompleteClick = {},
    )
}

@Preview
@Composable
private fun PhotographerDetailReservationBottomButtonsReservedPreview() {
    PhotographerDetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.RESERVED,
        onChatClick = {},
        onDealCompleteClick = {},
    )
}

@Preview
@Composable
private fun PhotographerDetailReservationBottomButtonsCompletedPreview() {
    PhotographerDetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.COMPLETED,
        onChatClick = {},
        onDealCompleteClick = {},
    )
}
