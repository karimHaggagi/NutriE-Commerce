package com.example.paymentstatus.model

import com.example.designsystem.Resources
import org.jetbrains.compose.resources.DrawableResource

sealed class PaymentStatusModel(
    val title: String,
    val subtitle: String,
    val image: DrawableResource
) {
    data object Success : PaymentStatusModel(
        title = "Success!",
        subtitle = "Your purchase is on the way.",
        image = Resources.Image.Checkmark
    )

    data class Failure(val message: String) : PaymentStatusModel(
        title = "Oops!",
        subtitle = message,
        image = Resources.Image.Cat
    )
}