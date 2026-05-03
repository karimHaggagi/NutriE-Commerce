package com.example.network

import com.example.model.Order
import com.example.model.RequestState
import com.example.network.ktor.dto.OrderRequest
import com.example.network.ktor.dto.OrderResponse
import com.example.network.ktor.dto.PayPalTokenDTO

interface CheckoutRemoteDataSource {
    suspend fun getToken(): RequestState<PayPalTokenDTO>
    suspend fun beginCheckout(order: OrderRequest,token: String,uniqueId: String): RequestState<OrderResponse>
}