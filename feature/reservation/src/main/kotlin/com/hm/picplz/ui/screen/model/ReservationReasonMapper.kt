package com.hm.picplz.ui.screen.model

import com.hm.picplz.ui.screen.cancel_reservation.CancelReason
import com.hm.picplz.ui.screen.photographer_cancel_reservation.PhotographerCancelReason
import com.hm.picplz.ui.screen.photographer_reject_reservation.PhotographerRejectReason

/**
 * 화면의 사유 선택지를 서버 enum 이름으로 변환합니다.
 *
 * 서버 사유 목록은 고객 취소 6종 + 작가 취소 6종(`SCHEDULE_CONFLICT` 공유)으로 총 11종이고,
 * 앱 선택지와 1:1로 대응합니다. `DIRECT_INPUT` 만 대응하는 서버 enum 이 없어
 * `*ReasonDetail` 문자열로만 전달되므로 null 을 반환합니다.
 *
 * 사유 enum 이 UI 계층에 있어 data 모듈에서 참조할 수 없기 때문에 매핑은 여기에 둡니다.
 * 선택지를 추가하면 아래 `when` 이 exhaustive 하지 않아 컴파일 에러가 납니다.
 */
fun CancelReason.toServerReason(): String? =
    when (this) {
        CancelReason.SCHEDULE -> "SCHEDULE_CONFLICT"
        CancelReason.PRODUCT -> "PRODUCT_CHANGE_REQUEST"
        CancelReason.LOCATION -> "LOCATION_ISSUE"
        CancelReason.MIND -> "CHANGE_OF_MIND"
        CancelReason.PHOTOGRAPHER_RESPONSE -> "PHOTOGRAPHER_NO_RESPONSE"
        CancelReason.PHOTOGRAPHER_EXTRA_PAYMENT -> "ADDITIONAL_PAYMENT_REQUEST"
        CancelReason.DIRECT_INPUT -> null
    }

fun PhotographerCancelReason.toServerReason(): String? =
    when (this) {
        PhotographerCancelReason.SCHEDULE -> "SCHEDULE_CONFLICT"
        PhotographerCancelReason.HEALTH -> "HEALTH_ISSUE"
        PhotographerCancelReason.CUSTOMER_RESPONSE -> "CUSTOMER_NO_RESPONSE"
        PhotographerCancelReason.MISTAKE -> "ACCIDENTAL_ACCEPTANCE"
        PhotographerCancelReason.EQUIPMENT -> "EQUIPMENT_FAILURE"
        PhotographerCancelReason.EXTERNAL -> "EXTERNAL_CIRCUMSTANCE"
        PhotographerCancelReason.DIRECT_INPUT -> null
    }

fun PhotographerRejectReason.toServerReason(): String? =
    when (this) {
        PhotographerRejectReason.AREA -> "REGION_UNAVAILABLE"
        PhotographerRejectReason.TIME -> "TIME_UNAVAILABLE"
        PhotographerRejectReason.EQUIPMENT -> "EQUIPMENT_ISSUE"
        PhotographerRejectReason.DIRECT_INPUT -> null
    }

/**
 * 선택된 사유들을 서버 enum 이름 목록으로 변환합니다.
 *
 * 선언 순서(= 화면 노출 순서)를 유지하고 `DIRECT_INPUT` 은 제외합니다.
 */
fun Set<CancelReason>.toServerReasons(): List<String> =
    CancelReason.entries.filter { it in this }.mapNotNull { it.toServerReason() }

@JvmName("photographerCancelReasonsToServerReasons")
fun Set<PhotographerCancelReason>.toServerReasons(): List<String> =
    PhotographerCancelReason.entries.filter { it in this }.mapNotNull { it.toServerReason() }
