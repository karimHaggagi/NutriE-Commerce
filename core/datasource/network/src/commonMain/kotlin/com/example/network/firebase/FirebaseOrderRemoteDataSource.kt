package com.example.network.firebase

import com.example.model.Order
import com.example.model.RequestState
import com.example.network.OrderRemoteDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow

class FirebaseOrderRemoteDataSource: OrderRemoteDataSource {
    private val productsCollection = Firebase.firestore.collection("orders")

    override fun createOrder(order: Order) = flow {
        try {
            productsCollection.document(order.id).set(order)
            emit(RequestState.Success(Unit))
        }catch (e: Exception){
            emit(RequestState.Error(e.message ?: ""))
        }
    }
}