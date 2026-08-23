package com.hm.picplz.data.mapper

import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.data.model.ReservationDetailDto
import com.hm.picplz.domain.model.ReservationDetail
import com.hm.picplz.domain.model.ReservationStatusCode

private const val EDITED_YES = "Y"

fun ReservationDetailDto.toReservationDetail(): ReservationDetail =
    ReservationDetail(
        reservationId = reservationId,
        packageName = packageName.orEmpty(),
        productPrice = productPrice ?: 0,
        editPrice = editPrice ?: 0,
        shootingDateTimeMillis = DateTimeUtil.parseServerDateTime(reservationTime),
        place = place.orEmpty(),
        photoAmount = photoAmount ?: 0,
        isEdited = editedYn == EDITED_YES,
        reservationNumber = reservationNumber.orEmpty(),
        status = ReservationStatusCode.from(status),
    )
