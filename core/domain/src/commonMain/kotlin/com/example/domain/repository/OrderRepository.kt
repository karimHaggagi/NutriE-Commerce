package com.example.domain.repository

import com.example.model.Customer
import com.example.model.Order
import com.example.model.PaymentData
import com.example.model.RequestState
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun createOrder(order: Order): Flow<RequestState<Unit>>
    fun beginCheckout(order: Order,customer: Customer): Flow<RequestState<String>>

    fun getPaymentData(): Flow<PaymentData>
    fun resetPaymentData()
}