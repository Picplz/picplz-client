package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.api.ReservationApi
import com.hm.picplz.data.model.CancelReservationRequest
import com.hm.picplz.data.model.RejectReservationRequest
import com.hm.picplz.data.model.ReservationDetailDto
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import javax.inject.Inject

interface ReservationSource {
    suspend fun getReservation(reservationId: Long): AppResult<ReservationDetailDto>

    suspend fun getPhotographerReservations(): AppResult<List<ReservationDetailDto>>

    suspend fun acceptReservation(reservationId: Long): AppResult<Unit>

    suspend fun rejectReservation(
        reservationId: Long,
        request: RejectReservationRequest,
    ): AppResult<Unit>

    suspend fun cancelReservation(
        reservationId: Long,
        request: CancelReservationRequest,
    ): AppResult<Unit>
}

class ReservationSourceImpl
    @Inject
    constructor(
        private val reservationApi: ReservationApi,
    ) : ReservationSource {
        override suspend fun getReservation(reservationId: Long): AppResult<ReservationDetailDto> =
            safeApiCall({ reservationApi.getReservation(reservationId) }) { it.data }

        override suspend fun getPhotographerReservations(): AppResult<List<ReservationDetailDto>> =
            safeApiCall({ reservationApi.getPhotographerReservations() }) { it.data }

        override suspend fun acceptReservation(reservationId: Long): AppResult<Unit> =
            safeApiCallUnit { reservationApi.acceptReservation(reservationId) }

        override suspend fun rejectReservation(
            reservationId: Long,
            request: RejectReservationRequest,
        ): AppResult<Unit> = safeApiCallUnit { reservationApi.rejectReservation(reservationId, request) }

        override suspend fun cancelReservation(
            reservationId: Long,
            request: CancelReservationRequest,
        ): AppResult<Unit> = safeApiCallUnit { reservationApi.cancelReservation(reservationId, request) }
    }
