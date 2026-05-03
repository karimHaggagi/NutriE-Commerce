package com.example.network

import com.example.model.Order
import com.example.model.RequestState
import kotlinx.coroutines.flow.Flow

interface OrderRemoteDataSource {
    fun createOrder(order: Order): Flow<RequestState<Unit>>
}