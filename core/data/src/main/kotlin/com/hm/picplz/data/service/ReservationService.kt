package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.mapper.toReservationDetail
import com.hm.picplz.data.model.CancelReservationRequest
import com.hm.picplz.data.model.RejectReservationRequest
import com.hm.picplz.data.source.ReservationSource
import com.hm.picplz.domain.model.ReservationDetail
import javax.inject.Inject

interface ReservationService {
    suspend fun getReservation(reservationId: Long): AppResult<ReservationDetail>

    suspend fun getPhotographerReservations(): AppResult<List<ReservationDetail>>

    suspend fun acceptReservation(reservationId: Long): AppResult<Unit>

    /**
     * @param reasons 서버 enum 이름 목록 (예: `REGION_UNAVAILABLE`)
     * @param reasonDetail 직접 입력 문구. 비어 있으면 null 로 보냅니다.
     */
    suspend fun rejectReservation(
        reservationId: Long,
        reasons: List<String>,
        reasonDetail: String?,
    ): AppResult<Unit>

    /**
     * 고객·작가 공용 취소.
     *
     * @param reasons 서버 enum 이름 목록 (예: `CHANGE_OF_MIND`)
     * @param reasonDetail 직접 입력 문구. 비어 있으면 null 로 보냅니다.
     */
    suspend fun cancelReservation(
        reservationId: Long,
        reasons: List<String>,
        reasonDetail: String?,
    ): AppResult<Unit>
}

class ReservationServiceImpl
    @Inject
    constructor(
        private val reservationSource: ReservationSource,
    ) : ReservationService {
        override suspend fun getReservation(reservationId: Long): AppResult<ReservationDetail> =
            reservationSource.getReservation(reservationId).map { it.toReservationDetail() }

        override suspend fun getPhotographerReservations(): AppResult<List<ReservationDetail>> =
            reservationSource.getPhotographerReservations().map { reservations ->
                reservations.map { it.toReservationDetail() }
            }

        override suspend fun acceptReservation(reservationId: Long): AppResult<Unit> =
            reservationSource.acceptReservation(reservationId)

        override suspend fun rejectReservation(
            reservationId: Long,
            reasons: List<String>,
            reasonDetail: String?,
        ): AppResult<Unit> =
            reservationSource.rejectReservation(
                reservationId,
                RejectReservationRequest(
                    rejectReasons = reasons,
                    rejectReasonDetail = reasonDetail?.takeIf { it.isNotBlank() },
                ),
            )

        override suspend fun cancelReservation(
            reservationId: Long,
            reasons: List<String>,
            reasonDetail: String?,
        ): AppResult<Unit> =
            reservationSource.cancelReservation(
                reservationId,
                CancelReservationRequest(
                    cancelReasons = reasons,
                    cancelReasonDetail = reasonDetail?.takeIf { it.isNotBlank() },
                ),
            )
    }
