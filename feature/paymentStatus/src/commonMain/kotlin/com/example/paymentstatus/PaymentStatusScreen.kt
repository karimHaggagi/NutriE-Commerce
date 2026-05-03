package com.example.paymentstatus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.*
import com.example.designsystem.component.InfoCard
import com.example.designsystem.component.PrimaryButton
import com.example.paymentstatus.model.PaymentStatusModel

@Composable
internal fun PaymentStatusScreen(
    modifier: Modifier = Modifier,
    model: PaymentStatusModel,
    navigateBack: ()-> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .systemBarsPadding()
            .padding(all = 24.dp)
    ) {
        Column {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                InfoCard(
                    title = model.title,
                    subtitle = model.subtitle,
                    image = model.image
                )
            }
            PrimaryButton(
                text = "Go back",
                icon = Resources.Icon.RightArrow,
                onClick = navigateBack
            )
        }
    }
}