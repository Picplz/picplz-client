package com.hm.picplz.ui.screen.model

import com.hm.picplz.domain.model.ReservationStatusCode
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

/**
 * 서버 예약 상태를 화면 상태로 변환합니다.
 *
 * 두 체계가 완전히 대응하지 않습니다.
 * - 서버 `REJECTED`/`CANCELED` 에 해당하는 화면 상태가 없습니다. 거절·취소 후에는 화면을 벗어나는
 *   플로우라 지금은 null 을 반환하고, 호출부가 기존 상태를 유지합니다.
 * - 화면의 `COMPLETED`(거래 완료)에 대응하는 서버 상태가 없어 역방향 매핑은 만들지 않습니다.
 */
fun ReservationStatusCode.toUiStatusOrNull(): ReservationStatus? =
    when (this) {
        ReservationStatusCode.PENDING -> ReservationStatus.WAITING_APPROVAL
        ReservationStatusCode.ACCEPTED -> ReservationStatus.WAITING_SCHEDULE
        ReservationStatusCode.CONFIRMED -> ReservationStatus.RESERVED
        ReservationStatusCode.REJECTED, ReservationStatusCode.CANCELED -> null
    }
