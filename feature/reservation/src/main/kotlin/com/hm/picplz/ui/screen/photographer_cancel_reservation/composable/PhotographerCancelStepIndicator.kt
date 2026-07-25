package com.hm.picplz.ui.screen.photographer_cancel_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.photographer_cancel_reservation.PhotographerCancelReservationState.Step
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerCancelStepIndicator(
    currentStep: Step,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepCircle(number = 1, isActive = currentStep == Step.REASON)
        StepCircle(number = 2, isActive = currentStep == Step.POLICY)
    }
}

@Composable
private fun StepCircle(
    number: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(24.dp)
                .background(
                    color = if (isActive) MainThemeColor.Olive else MainThemeColor.Gray2,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = MainThemeFont.BodyBold,
            color = if (isActive) MainThemeColor.White else MainThemeColor.Gray3,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotographerCancelStepIndicatorReasonPreview() {
    PicplzTheme {
        PhotographerCancelStepIndicator(currentStep = Step.REASON)
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotographerCancelStepIndicatorPolicyPreview() {
    PicplzTheme {
        PhotographerCancelStepIndicator(currentStep = Step.POLICY)
    }
}
