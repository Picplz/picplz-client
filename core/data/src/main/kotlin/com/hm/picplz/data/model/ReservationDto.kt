package com.hm.picplz.data.model

/**
 * 예약 상세 응답.
 *
 * `GET /api/v1/reservations/{reservationId}` 와 `GET /api/v1/photographers/reservations` 가
 * 같은 형태를 사용합니다. 둘 다 **작가 전용** 엔드포인트로, 고객 토큰으로 호출하면
 * `404 PHOTOGRAPHER_404_1` 이 떨어집니다.
 *
 * 응답에 고객 정보(이름/프로필)와 확정 시각이 없어, 화면에서 필요한 값은 아직 더미로 남아 있습니다.
 */
data class ReservationDetailDto(
    val reservationId: Long,
    val packageName: String?,
    val productPrice: Int?,
    val editPrice: Int?,
    /** ISO-8601 local date-time. 예: `2026-09-01T14:00:00` */
    val reservationTime: String?,
    val place: String?,
    val photoAmount: Int?,
    /** `"Y"` / `"N"` */
    val editedYn: String?,
    val reservationNumber: String?,
    /** `PENDING` / `ACCEPTED` / `CONFIRMED` / `REJECTED` / `CANCELED` */
    val status: String?,
)

/**
 * 예약 취소 요청. 고객·작가 공용 엔드포인트입니다.
 *
 * [cancelReasons] 는 서버 enum 이름 목록입니다. 앱의 사유 enum → 서버 문자열 변환은
 * `feature/reservation` 의 매퍼가 담당합니다(사유 enum이 UI 계층에 있어 data 모듈에서 참조 불가).
 */
data class CancelReservationRequest(
    val cancelReasons: List<String>,
    val cancelReasonDetail: String?,
)

/** 예약 거절 요청. 작가 전용입니다. */
data class RejectReservationRequest(
    val rejectReasons: List<String>,
    val rejectReasonDetail: String?,
)
