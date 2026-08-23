package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.CancelReservationRequest
import com.hm.picplz.data.model.RejectReservationRequest
import com.hm.picplz.data.model.ReservationDetailDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

/**
 * 예약 API.
 *
 * 상태 전이는 `PENDING` --(작가 accept)--> `ACCEPTED` --(고객 confirm)--> `CONFIRMED` 순서이고,
 * 취소는 어느 단계에서든 가능합니다. 각 엔드포인트는 호출자 역할을 검증하므로
 * 작가 전용 API를 고객 토큰으로 부르면 `404 PHOTOGRAPHER_404_1` 이 돌아옵니다.
 *
 * 고객의 일자 확정(`PATCH .../confirm`)은 별도 화면이 필요해 이 인터페이스에 아직 없습니다.
 */
interface ReservationApi {
    /** 작가 전용. 고객용 예약 상세 조회 API는 서버에 아직 없습니다. */
    @GET("api/v1/reservations/{reservationId}")
    suspend fun getReservation(
        @Path("reservationId") reservationId: Long,
    ): Response<ApiResponse<ReservationDetailDto>>

    /** 작가가 받은 예약 목록. */
    @GET("api/v1/photographers/reservations")
    suspend fun getPhotographerReservations(): Response<ApiResponse<List<ReservationDetailDto>>>

    /** 작가의 예약 승인. 요청 본문이 없습니다. */
    @PATCH("api/v1/reservations/{reservationId}/accept")
    suspend fun acceptReservation(
        @Path("reservationId") reservationId: Long,
    ): Response<Unit>

    /** 작가의 예약 거절. */
    @PATCH("api/v1/reservations/{reservationId}/reject")
    suspend fun rejectReservation(
        @Path("reservationId") reservationId: Long,
        @Body request: RejectReservationRequest,
    ): Response<Unit>

    /** 고객·작가 공용 예약 취소. */
    @PATCH("api/v1/reservations/{reservationId}/cancel")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: Long,
        @Body request: CancelReservationRequest,
    ): Response<Unit>
}
