package com.example.local

import com.example.model.PaymentData
import kotlinx.coroutines.flow.Flow

interface PaymentDataSource {

    fun setPaymentData(
        isSuccess: Boolean?,
        errorMessage: String? = null,
        token: String? = null
    )

    fun getPaymentData(): Flow<PaymentData>

    fun reset()
}