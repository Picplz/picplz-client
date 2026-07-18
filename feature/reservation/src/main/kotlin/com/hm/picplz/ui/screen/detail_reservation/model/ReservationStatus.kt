package com.hm.picplz.ui.screen.detail_reservation.model

import androidx.annotation.StringRes
import com.hm.picplz.feature.reservation.R

/**
 * 예약 상태.
 *
 * 헤더 타이틀/설명은 고객·작가 관점이 다르므로 role별 리소스를 각각 보유합니다.
 * (고객 화면: customer*, 작가 화면: photographer*)
 */
enum class ReservationStatus(
    val step: ReservationStep,
    @get:StringRes val customerTitleResId: Int,
    @get:StringRes val customerDescriptionResId: Int,
    @get:StringRes val photographerTitleResId: Int,
    @get:StringRes val photographerDescriptionResId: Int,
) {
    WAITING_APPROVAL(
        step = ReservationStep.WAITING,
        customerTitleResId = R.string.reservation_status_title_waiting_approval,
        customerDescriptionResId = R.string.reservation_status_description_waiting_approval,
        photographerTitleResId = R.string.reservation_status_title_waiting_approval,
        photographerDescriptionResId = R.string.reservation_status_description_waiting_approval,
    ),
    WAITING_SCHEDULE(
        step = ReservationStep.WAITING,
        customerTitleResId = R.string.reservation_status_customer_title_waiting_schedule,
        customerDescriptionResId = R.string.reservation_status_customer_description_waiting_schedule,
        photographerTitleResId = R.string.reservation_status_title_waiting_payment,
        photographerDescriptionResId = R.string.reservation_status_description_waiting_payment,
    ),
    RESERVED(
        step = ReservationStep.IN_PROGRESS,
        customerTitleResId = R.string.reservation_status_customer_title_reserved,
        customerDescriptionResId = R.string.reservation_status_description_reserved,
        photographerTitleResId = R.string.reservation_status_title_reserved,
        photographerDescriptionResId = R.string.reservation_status_description_reserved,
    ),
    COMPLETED(
        step = ReservationStep.CONFIRMED,
        customerTitleResId = R.string.reservation_status_customer_title_completed,
        customerDescriptionResId = R.string.reservation_status_description_completed,
        photographerTitleResId = R.string.reservation_status_title_completed,
        photographerDescriptionResId = R.string.reservation_status_description_completed,
    ),
    ;

    companion object {
        fun ReservationStatus.showCancelButton(): Boolean = this != COMPLETED
    }
}
