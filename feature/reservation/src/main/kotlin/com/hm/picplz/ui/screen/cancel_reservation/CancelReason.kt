package com.hm.picplz.ui.screen.cancel_reservation

import com.hm.picplz.feature.reservation.R

/**
 * 취소 사유 선택지.
 *
 * 주의: 선언 순서 = 화면 노출 순서입니다.
 */
enum class CancelReason(
    val stringRes: Int,
) {
    SCHEDULE(R.string.cancel_reason_option_schedule),
    PRODUCT(R.string.cancel_reason_option_product),
    LOCATION(R.string.cancel_reason_option_location),
    MIND(R.string.cancel_reason_option_mind),
    PHOTOGRAPHER_RESPONSE(R.string.cancel_reason_option_photographer_response),
    PHOTOGRAPHER_EXTRA_PAYMENT(R.string.cancel_reason_option_photographer_extra_payment),
    DIRECT_INPUT(R.string.cancel_reason_option_direct_input),
}
