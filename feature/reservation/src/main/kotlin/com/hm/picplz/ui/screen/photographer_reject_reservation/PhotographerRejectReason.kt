package com.hm.picplz.ui.screen.photographer_reject_reservation

import com.hm.picplz.feature.reservation.R

/** 직접 입력 거절 사유 최대 글자 수 */
const val REJECT_REASON_MAX_LENGTH = 20

/**
 * 작가 예약 거절 사유. 취소 사유와 달리 단일 선택이며, 거절 전용 문구를 사용합니다.
 */
enum class PhotographerRejectReason(val stringRes: Int) {
    AREA(R.string.reject_reason_option_area),
    TIME(R.string.reject_reason_option_time),
    EQUIPMENT(R.string.reject_reason_option_equipment),
    DIRECT_INPUT(R.string.reject_reason_option_direct_input),
}
