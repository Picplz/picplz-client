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
)
