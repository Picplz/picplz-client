package com.hm.picplz.ui.screen.photographer_reject_reservation

sealed interface PhotographerRejectReservationSideEffect {
    /** 상단 뒤로가기 → 이전 화면(예약 상세)으로 복귀 */
    data object NavigateBack : PhotographerRejectReservationSideEffect

    /** 거절 확정 → 예약이 시작된 채팅방으로 복귀 */
    data object NavigateToChat : PhotographerRejectReservationSideEffect
}
