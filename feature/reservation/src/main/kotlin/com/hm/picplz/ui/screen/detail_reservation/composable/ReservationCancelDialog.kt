package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.core.ui.R as CoreR

@Composable
fun ReservationCancelDialog(
    status: ReservationStatus,
    refundCondition: RefundCondition,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonButtonModal(
        modifier = modifier,
        onDismissRequest = onDismiss,
        cancelText = stringResource(R.string.reservation_cancel_dialog_button_no),
        confirmText = stringResource(R.string.reservation_cancel_dialog_button_yes),
        onCancel = onCancel,
        onConfirm = onConfirm,
    ) {
        ReservationCancelDialogContent(
            status = status,
            refundCondition = refundCondition,
            onInfoClick = onInfoClick,
        )
    }
}

@Composable
private fun ReservationCancelDialogContent(
    status: ReservationStatus,
    refundCondition: RefundCondition,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 10.dp,
                    bottom = 20.dp,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (status == ReservationStatus.RESERVED) {
            Image(
                painter = painterResource(CoreR.drawable.info),
                contentDescription = "환불 규정 안내",
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .size(16.dp)
                        .clickable(onClick = onInfoClick),
            )
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
        Text(
            text = stringResource(R.string.reservation_cancel_dialog_title),
            style = pretendardTypography.titleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (status == ReservationStatus.RESERVED && !refundCondition.isFullRefund()) {
            Text(
                text = getPartialRefundAnnotatedText(refundPercent = refundCondition.percent),
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = getDescriptionText(status, refundCondition),
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun getDescriptionText(
    status: ReservationStatus,
    refundCondition: RefundCondition,
): String =
    when (status) {
        ReservationStatus.RESERVED -> {
            when (refundCondition) {
                RefundCondition.WITHIN_24_HOURS,
                RefundCondition.BEFORE_7_DAYS,
                -> {
                    stringResource(R.string.reservation_cancel_dialog_desc_full_refund)
                }

                else -> {
                    stringResource(
                        R.string.reservation_cancel_dialog_desc_partial_refund,
                        refundCondition.percent,
                    )
                }
            }
        }

        ReservationStatus.WAITING_APPROVAL,
        ReservationStatus.WAITING_SCHEDULE,
        -> {
            stringResource(R.string.reservation_cancel_dialog_desc_waiting_approval)
        }

        ReservationStatus.COMPLETED -> {
            ""
        }
    }

@Composable
private fun getPartialRefundAnnotatedText(refundPercent: Int) =
    buildAnnotatedString {
        append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_prefix))
        withStyle(style = SpanStyle(color = MainThemeColor.Red)) {
            append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_highlight, refundPercent))
        }
        append(stringResource(R.string.reservation_cancel_dialog_desc_partial_refund_suffix))
    }

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ReservationCancelDialogWaitingApprovalPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.WAITING_APPROVAL,
        refundCondition = RefundCondition.WITHIN_24_HOURS,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
        onInfoClick = {},
    )
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ReservationCancelDialogFullRefundPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.RESERVED,
        refundCondition = RefundCondition.BEFORE_7_DAYS,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
        onInfoClick = {},
    )
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ReservationCancelDialogPartialRefundPreview() {
    ReservationCancelDialog(
        status = ReservationStatus.RESERVED,
        refundCondition = RefundCondition.BEFORE_3_DAYS,
        onDismiss = {},
        onCancel = {},
        onConfirm = {},
        onInfoClick = {},
    )
}
