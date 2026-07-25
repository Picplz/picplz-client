package com.hm.picplz.ui.screen.photographer_cancel_reservation

import com.hm.picplz.feature.reservation.R

/**
 * 작가 예약 취소 사유. 고객 취소 사유([com.hm.picplz.ui.screen.cancel_reservation.CancelReason])와
 * 문구가 다르기 때문에 별도로 정의합니다.
 */
enum class PhotographerCancelReason(val stringRes: Int) {
    SCHEDULE(R.string.photographer_cancel_reason_schedule),
    HEALTH(R.string.photographer_cancel_reason_health),
    CUSTOMER_RESPONSE(R.string.photographer_cancel_reason_customer_response),
    MISTAKE(R.string.photographer_cancel_reason_mistake),
    EQUIPMENT(R.string.photographer_cancel_reason_equipment),
    EXTERNAL(R.string.photographer_cancel_reason_external),
    DIRECT_INPUT(R.string.photographer_cancel_reason_direct_input),
}
