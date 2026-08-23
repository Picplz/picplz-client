package com.hm.picplz.domain.model

/**
 * 서버가 관리하는 예약 상태.
 *
 * UI의 `ReservationStatus`(예약 대기 / 일시 확정 대기 / 예약 확정 / 거래 완료)와는 별개입니다.
 * 서버엔 "거래 완료"에 해당하는 값이 없고, 반대로 [REJECTED]·[CANCELED] 는 UI enum에 없습니다.
 * 두 체계를 잇는 매핑은 `feature/reservation` 이 담당합니다.
 */
enum class ReservationStatusCode {
    /** 작가 승인 대기 */
    PENDING,

    /** 작가가 승인함. 고객의 일자 확정 대기 */
    ACCEPTED,

    /** 고객이 일자를 확정함 */
    CONFIRMED,

    /** 작가가 거절함 */
    REJECTED,

    /** 고객 또는 작가가 취소함 */
    CANCELED,

    ;

    companion object {
        /** 서버 문자열 → enum. 모르는 값이면 null 을 반환합니다. */
        fun from(raw: String?): ReservationStatusCode? = entries.firstOrNull { it.name == raw }
    }
}

/**
 * 예약 상세.
 *
 * 서버 응답에 고객 정보(닉네임/프로필)와 확정 시각이 없어 화면에서 필요한 일부 값은
 * 아직 채울 수 없습니다.
 */
data class ReservationDetail(
    val reservationId: Long,
    val packageName: String,
    val productPrice: Int,
    val editPrice: Int,
    /** 촬영 예정 일시(ms). 서버 값이 없거나 파싱에 실패하면 null */
    val shootingDateTimeMillis: Long?,
    val place: String,
    val photoAmount: Int,
    val isEdited: Boolean,
    val reservationNumber: String,
    /** 서버가 모르는 상태 문자열을 내려주면 null */
    val status: ReservationStatusCode?,
)
