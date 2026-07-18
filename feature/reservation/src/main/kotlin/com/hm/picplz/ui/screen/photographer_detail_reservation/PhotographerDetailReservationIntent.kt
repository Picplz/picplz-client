package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationIntent {
    data object NavigateToChat : PhotographerDetailReservationIntent

    data object ApproveReservation : PhotographerDetailReservationIntent

    data object ConfirmReservation : PhotographerDetailReservationIntent

    data object ShowCancelDialog : PhotographerDetailReservationIntent

    data object DismissCancelDialog : PhotographerDetailReservationIntent

    data object ConfirmCancel : PhotographerDetailReservationIntent

    data object ShowRefundPolicyDialog : PhotographerDetailReservationIntent

    data object DismissRefundPolicyTooltip : PhotographerDetailReservationIntent

    data object NavigateBack : PhotographerDetailReservationIntent
}
