package com.hm.picplz.ui.screen.detail_reservation

sealed interface DetailReservationIntent {
    data object NavigateToChat : DetailReservationIntent

    data object NavigateToWriteReview : DetailReservationIntent

    data object ConfirmReservation : DetailReservationIntent

    data object ShowCancelDialog : DetailReservationIntent

    data object DismissCancelDialog : DetailReservationIntent

    data object ConfirmCancel : DetailReservationIntent

    data object ShowRefundPolicyDialog : DetailReservationIntent

    data object DismissRefundPolicyTooltip : DetailReservationIntent

    data object NavigateBack : DetailReservationIntent
}
