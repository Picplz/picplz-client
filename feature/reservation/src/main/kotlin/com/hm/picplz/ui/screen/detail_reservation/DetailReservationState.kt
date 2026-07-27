package com.hm.picplz.ui.screen.detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

data class DetailReservationState(
    val orderId: String = "",
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    val hasWrittenReview: Boolean = false,
    val showCancelDialog: Boolean = false,
    val shootingDateTimeMillis: Long = System.currentTimeMillis(),
    val confirmedDateTimeMillis: Long = System.currentTimeMillis(),
    val refundCondition: RefundCondition = RefundCondition.WITHIN_24_HOURS,
    val showRefundPolicyTooltip: Boolean = false,
    /** "거래 완료 되었습니다" 모달 노출 여부. 잠시 보여준 뒤 리뷰 작성 화면으로 넘어갑니다. */
    val showDealCompleteModal: Boolean = false,
)
