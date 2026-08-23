package com.hm.picplz.ui.screen.photographer_detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

data class PhotographerDetailReservationState(
    val reservationId: Long = 0L,
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    /** 서버 `reservationTime` 을 매핑한 촬영 예정 일시 */
    val shootingDateTimeMillis: Long = System.currentTimeMillis(),
    /** 서버 `packageName`. 비어 있으면 정보 섹션이 기본값을 씁니다. */
    val packageName: String = "",
    /** 서버 `place`. 비어 있으면 정보 섹션이 기본값을 씁니다. */
    val place: String = "",
    /** 서버 예약 상세 응답에 고객 정보가 없어 더미를 유지합니다. */
    val customerName: String = DUMMY_CUSTOMER_NAME,
    val isLoading: Boolean = false,
    /**
     * 마지막으로 띄운 토스트 문구. 퇴장 애니메이션이 끝날 때까지 남아 있어야 하므로
     * [showToast]가 false가 되어도 지우지 않습니다.
     */
    val toastMessageResId: Int? = null,
    val showToast: Boolean = false,
)

const val DUMMY_CUSTOMER_NAME = "애니프사"
