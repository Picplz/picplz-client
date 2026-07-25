package com.hm.picplz.ui.screen.photographer_detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

data class PhotographerDetailReservationState(
    val orderId: String = "",
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    val shootingDateTimeMillis: Long = System.currentTimeMillis(),
    val confirmedDateTimeMillis: Long = System.currentTimeMillis(),
    val customerName: String = DUMMY_CUSTOMER_NAME,
)

const val DUMMY_CUSTOMER_NAME = "애니프사"
